package com.cmcc.rtls.controller;


import com.cmcc.rtls.service.WordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author xiuyin.cui
 * @Description
 * @date 2019/3/22 10:52
 */
@Controller
public class IndexController {
    @Autowired
    private WordService tableService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${server.port}")
    private Integer port;
    @Value("${swagger.url}")
    private String swaggerUrl;


    @RequestMapping("/")
    public String home(Model model) {
        return "redirect:index.html";
    }

    /**
     * 将 swagger 文档转换成 html 文档，可通过在网页上右键另存为 xxx.doc 的方式转换为 word 文档
     * @param model
     * @param url   需要转换成 word 文档的资源地址
     * @return
     */
    @Deprecated
    @RequestMapping("/download")
    public String download(Model model, @RequestParam(value = "url", required = false) String url) {
        url = StringUtils.defaultIfBlank(url, swaggerUrl);
        Map<String, Object> result = tableService.tableList(url,false);
        model.addAttribute("url", url);
        model.addAllAttributes(result);
        return "download";
    }

    /**
     * 将 swagger 文档一键下载为 doc 文档
     *
     * @param url      需要转换成 word 文档的资源地址
     * @param response
     */
    @RequestMapping("/downloadWord")
    public void word(@RequestParam(required = false) String url, HttpServletResponse response) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/download?url=" + url, String.class);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("apiWord.doc", "utf-8"));
            byte[] bytes = forEntity.getBody().getBytes();
            bos.write(bytes, 0, bytes.length);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
