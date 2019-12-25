package com.cmcc.rtls.service.impl;

import com.cmcc.rtls.model.*;
import com.cmcc.rtls.service.MenuService;
import com.cmcc.rtls.service.WordService;
import com.cmcc.rtls.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @Author XiuYin.Cui
 * @Date 2018/1/12
 **/
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MenuService menuService;
    private Boolean initData = Boolean.TRUE;
    private Map<String, Object> resultMap = new HashMap<>();

    @Override
    @Transactional
    public Map<String, Object> tableList(String swaggerUrl) {
        if (initData) {
            System.out.println("同步数据中...");
            List<Table> result = new ArrayList<>();
            menuService.synchronization();
            try {
                String jsonStr = restTemplate.getForObject(swaggerUrl, String.class);
                // convert JSON string to Map
                Map<String, Object> map = JsonUtils.readValue(jsonStr, HashMap.class);

                //解析model
                //Map<String, ModelAttr> definitinMap = parseDefinitions(map);

                //解析paths
                Map<String, Map<String, Object>> paths = (Map<String, Map<String, Object>>) map.get("paths");
                if (paths != null) {
                    Iterator<Entry<String, Map<String, Object>>> it = paths.entrySet().iterator();
                    while (it.hasNext()) {
                        Entry<String, Map<String, Object>> path = it.next();

                        Iterator<Entry<String, Object>> it2 = path.getValue().entrySet().iterator();
                        Iterator iterator = path.getValue().keySet().iterator();
                        while (it2.hasNext()) {
                            // 1.请求路径
                            String url = path.getKey();

                            // 2.请求方式，类似为 get,post,delete,put 这样
                            String requestType = iterator.next().toString();
                            //String requestType = StringUtils.join(path.getValue().keySet(), ",");

                            // 3. 解析多种请求方式
                            Entry<String, Object> firstRequest = it2.next();
                            Map<String, Object> content = (Map<String, Object>) firstRequest.getValue();

                            // 4. 大标题（类说明）
                            String title = String.valueOf(((List) content.get("tags")).get(0));

                            // 5.小标题 （方法说明）
                            String tag = String.valueOf(content.get("summary"));

                            // 6.接口描述
                            String description = String.valueOf(content.get("description"));

                            // 7.请求参数格式，类似于 multipart/form-data
                            String requestForm = "*/*";
                            List<String> consumes = (List) content.get("consumes");
                            if (consumes != null && consumes.size() > 0) {
                                requestForm = StringUtils.join(consumes, ",");
                            }

                            // 8.返回参数格式，类似于 application/json
                            String responseForm = "*/*";
                            List<String> produces = (List) content.get("produces");
                            if (produces != null && produces.size() > 0) {
                                responseForm = StringUtils.join(produces, ",");
                            }

                            // 9. 请求体
                            List<LinkedHashMap> parameters = (ArrayList) content.get("parameters");

                            // 10.返回体
                            Map<String, Object> responses = (LinkedHashMap) content.get("responses");

                            //封装Table
                            Table table = new Table();

                            //插入接口信息到数据库
                            Menu menu = new Menu();
                            menu.setUrl(map.get("basePath") + url);
                            menu.setType(3);
                            menu.setTitle(description);
                            menu.setPid(0);
                            menu.setRemark(tag);
                            menu.setMethod(requestType);
                            menu.setIcon("");
                            menuService.insert(menu);

                            table.setTitle(title);
                            table.setUrl(url);
                            table.setTag(tag);
                            table.setDescription(description);
                            table.setRequestForm(requestForm);
                            table.setResponseForm(responseForm);
                            table.setRequestType(requestType);
                            table.setResponseList(processResponseCodeList(responses));
                            List<Request> requestsList = processRequestList(parameters);
                            table.setRequestParam(JsonUtils.writeJsonStr(buildParamMap(requestsList, map)));
                            for (Request request : requestsList) {
                                request.setParamType(request.getParamType().replaceAll("#/definitions/", ""));
                            }
                            table.setRequestList(requestsList);
                            // 取出来状态是200时的返回值
                            Object obj = responses.get("200");
                            if (obj == null) {
                                table.setResponseParam("");
                                result.add(table);
                                continue;
                            }
                            Object schema = ((Map) obj).get("schema");
                            if (schema == null) {
                                table.setResponseParam("");
                                result.add(table);
                                continue;
                            }
                            if (((Map) schema).get("$ref") != null) {
                                //非数组类型返回值
                                String ref = (String) ((Map) schema).get("$ref");
                                //解析swagger2 ref链接
                                ObjectNode objectNode = parseRef(ref, map);
                                table.setResponseParam(objectNode.toString());
                                result.add(table);
                                continue;
                            }
                            Object items = ((Map) schema).get("items");
                            if (items != null && ((Map) items).get("$ref") != null) {
                                //数组类型返回值
                                String ref = (String) ((Map) items).get("$ref");
                                //解析swagger2 ref链接
                                ObjectNode objectNode = parseRef(ref, map);
                                ArrayNode arrayNode = JsonUtils.createArrayNode();
                                arrayNode.add(objectNode);
                                table.setResponseParam(arrayNode.toString());
                                result.add(table);
                                continue;
                            }
                            result.add(table);
                        }
                    }
                }
                Map<String, List<Table>> tableMap = result.stream().parallel().collect(Collectors.groupingBy(Table::getTitle));
                this.resultMap.put("tableMap", new TreeMap<>(tableMap));
                this.resultMap.put("info", map.get("info"));
                this.resultMap.put("basePath", map.get("basePath"));

                log.debug(JsonUtils.writeJsonStr(resultMap));
            } catch (Exception e) {
                log.error(e.getMessage());
                System.out.println("数据同步失败...");
                //事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                this.resultMap.put("info", "null");
                this.resultMap.put("basePath", "null");
                this.resultMap.put("tableMap", "null");
            }
            this.initData = false;
        }
        return this.resultMap;
    }

    /**
     * 封装post请求体
     *
     * @param list
     * @param map
     * @return
     */
    private Map<String, Object> buildParamMap(List<Request> list, Map<String, Object> map) throws IOException {
        Map<String, Object> paramMap = new HashMap<>(8);
        if (list != null && list.size() > 0) {
            for (Request request : list) {
                String name = request.getName();
                String type = request.getType();
                switch (type) {
                    case "string":
                        paramMap.put(name, "string");
                        break;
                    case "integer":
                        paramMap.put(name, 0);
                        break;
                    case "number":
                        paramMap.put(name, 0.0);
                        break;
                    case "boolean":
                        paramMap.put(name, true);
                        break;
                    case "body":
                        String paramType = request.getParamType();
                        ObjectNode objectNode = parseRef(paramType, map);
                        paramMap = JsonUtils.readValue(objectNode.toString(), Map.class);
                        break;
                    default:
                        paramMap.put(name, null);
                        break;
                }
            }
        }
        return paramMap;
    }

    /**
     * 从map中解析出指定的ref
     *
     * @param ref ref链接 例如："#/definitions/PageInfoBT«Customer»"
     * @param map 是整个swagger json转成map对象
     * @return
     * @author fpzhan
     */
    private ObjectNode parseRef(String ref, Map<String, Object> map) {
        ObjectNode objectNode = JsonUtils.createObjectNode();
        if (StringUtils.isNotEmpty(ref) && ref.startsWith("#")) {
            String[] refs = ref.split("/");
            Map<String, Object> tmpMap = map;
            //取出ref最后一个参数 start
            for (String tmp : refs) {
                if (!"#".equals(tmp)) {
                    tmpMap = (Map<String, Object>) tmpMap.get(tmp);
                }
            }
            //取出ref最后一个参数 end
            //取出参数
            if (tmpMap == null) {
                return objectNode;
            }
            Object properties = tmpMap.get("properties");
            if (properties == null) {
                return objectNode;
            }
            Map<String, Object> propertiesMap = (Map<String, Object>) properties;
            Set<String> keys = propertiesMap.keySet();
            //遍历key
            for (String key : keys) {
                Map<String, Object> keyMap = (Map) propertiesMap.get(key);
                if ("array".equals(keyMap.get("type"))) {
                    //数组的处理方式
                    String sonRef = (String) ((Map) keyMap.get("items")).get("$ref");
                    //对象自包含，跳过解析
                    if (ref.equals(sonRef)) {
                        continue;
                    }
                    JsonNode jsonNode = parseRef(sonRef, map);
                    ArrayNode arrayNode = JsonUtils.createArrayNode();
                    arrayNode.add(jsonNode);
                    objectNode.set(key, arrayNode);
                } else if (keyMap.get("$ref") != null) {
                    //对象的处理方式
                    String sonRef = (String) keyMap.get("$ref");
                    //对象自包含，跳过解析
                    if (ref.equals(sonRef)) {
                        continue;
                    }
                    ObjectNode object = parseRef(sonRef, map);
                    objectNode.set(key, object);
                } else {
                    //其他参数的处理方式，string、int
                    String str = "";
                    if (keyMap.get("description") != null) {
                        str = str + keyMap.get("description");
                    }
                    if (keyMap.get("format") != null) {
                        str = str + String.format("格式为(%s)", keyMap.get("format"));
                    }
                    objectNode.put(key, str);
                }
            }
        }
        return objectNode;
    }

    /**
     * 处理请求参数列表
     *
     * @param parameters
     * @return
     */
    private List<Request> processRequestList(List<LinkedHashMap> parameters) {
        List<Request> requestList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parameters)) {
            if (!CollectionUtils.isEmpty(parameters)) {
                for (Map<String, Object> param : parameters) {
                    Request request = new Request();
                    request.setName(String.valueOf(param.get("name")));
                    Object in = param.get("in");
                    if (in != null && "body".equals(in)) {
                        request.setType(String.valueOf(in));
                        Map<String, Object> schema = (Map) param.get("schema");
                        Object ref = schema.get("$ref");
                        // 数组情况另外处理
                        if (schema.get("type") != null && "array".equals(schema.get("type"))) {
                            ref = ((Map) schema.get("items")).get("$ref");
                        }
                        request.setParamType(ref == null ? "{}" : ref.toString());
                    } else {
                        request.setType(param.get("type") == null ? "Object" : param.get("type").toString());
                        request.setParamType(String.valueOf(in));
                    }
                    if (param.get("required") != null) {
                        request.setRequire((Boolean) param.get("required"));
                    } else {
                        request.setRequire(false);
                    }
                    request.setRemark(String.valueOf(param.get("description")));
                    requestList.add(request);
                }
            }
        }
        return requestList;
    }


    /**
     * 处理返回码列表
     *
     * @param responses 全部状态码返回对象
     * @return
     */
    private List<Response> processResponseCodeList(Map<String, Object> responses) {
        List<Response> responseList = new ArrayList<>();
        Iterator<Entry<String, Object>> resIt = responses.entrySet().iterator();
        while (resIt.hasNext()) {
            Entry<String, Object> entry = resIt.next();
            Response response = new Response();
            // 状态码 200 201 401 403 404 这样
            response.setName(entry.getKey());
            LinkedHashMap<String, Object> statusCodeInfo = (LinkedHashMap) entry.getValue();
            response.setDescription(String.valueOf(statusCodeInfo.get("description")));
            response.setRemark(String.valueOf(statusCodeInfo.get("description")));
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 解析Definition
     *
     * @param map
     * @return
     */
    private Map<String, ModelAttr> parseDefinitions(Map<String, Object> map) {
        Map<String, Map<String, Object>> definitions = (Map<String, Map<String, Object>>) map.get("definitions");
        Map<String, ModelAttr> definitinMap = new HashMap<>(256);
        if (definitions != null) {
            Iterator<String> modelNameIt = definitions.keySet().iterator();
            while (modelNameIt.hasNext()) {
                String modeName = modelNameIt.next();
                Map<String, Object> modeProperties = (Map<String, Object>) definitions.get(modeName).get("properties");
                if (modeProperties == null) {
                    continue;
                }
                Iterator<Entry<String, Object>> mIt = modeProperties.entrySet().iterator();

                List<ModelAttr> attrList = new ArrayList<>();

                //解析属性
                while (mIt.hasNext()) {
                    Entry<String, Object> mEntry = mIt.next();
                    Map<String, Object> attrInfoMap = (Map<String, Object>) mEntry.getValue();
                    ModelAttr modeAttr = new ModelAttr();
                    modeAttr.setName(mEntry.getKey());
                    modeAttr.setType((String) attrInfoMap.get("type"));
                    if (attrInfoMap.get("format") != null) {
                        modeAttr.setType(modeAttr.getType() + "(" + attrInfoMap.get("format") + ")");
                    }
                    modeAttr.setType(StringUtils.defaultIfBlank(modeAttr.getType(), "object"));
                    modeAttr.setDescription((String) attrInfoMap.get("description"));
                    attrList.add(modeAttr);
                }

                ModelAttr modeAttr = new ModelAttr();
                Object title = definitions.get(modeName).get("title");
                Object description = definitions.get(modeName).get("description");
                modeAttr.setClassName(title == null ? "" : title.toString());
                modeAttr.setDescription(description == null ? "" : description.toString());
                modeAttr.setProperties(attrList);
                definitinMap.put("#/definitions/" + modeName, modeAttr);
            }
        }
        return definitinMap;
    }
}
