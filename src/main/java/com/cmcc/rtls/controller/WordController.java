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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by XiuYin.Cui on 2018/1/11.
 */
@RestController
public class WordController {

    @Autowired
    private WordService tableService;

    @Value("${swagger.url}")
    private String swaggerUrl;

    @RequestMapping("/load")
    public Map<String, Object> load(Model model,@RequestParam(value = "url", required = false) String url) {
        Map<String, Object> result = tableService.tableList(StringUtils.defaultIfBlank(url, swaggerUrl), false);
        return result;
    }

    /**
     * 将 swagger 文档转换成 html 文档，可通过在网页上右键另存为 xxx.doc 的方式转换为 word 文档
     *
     * @param model
     * @param url   需要转换成 word 文档的资源地址
     * @return
     */
    @Deprecated
    @RequestMapping("/toWord")
    public Map<String, Object> getWord(Model model, @RequestParam(value = "url", required = true) String url,@RequestParam(value = "refresh", required = false) boolean refresh) {
        Map<String, Object> result = tableService.tableList(url, refresh);
        return result;
    }
}
