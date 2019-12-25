package com.cmcc.rtls.swaggerconvertword;

import com.cmcc.rtls.model.Menu;
import com.cmcc.rtls.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SwaggerconvertwordApplicationTests {

    @Autowired
    MenuService menuService;

    @Test
    void contextLoads() {
        Menu menu = new Menu();
        menu.setMethod("get");
        menu.setIcon("a");
        menu.setPid(0);
        menu.setRemark("");
        menu.setTitle("查询地图列表");
        menu.setType(3);
        menu.setUrl("/v1/map");
        menuService.insert(menu);
    }

}
