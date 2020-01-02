package com.cmcc.rtls.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.cmcc.rtls.config.DBContextHolder;
import com.cmcc.rtls.config.DynamicDataSource;
import com.cmcc.rtls.model.DatabaseSource;
import com.cmcc.rtls.service.impl.DBChangeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.cmcc.rtls.controller
 * @ClassName:DataSourceController
 * @Description:
 * @Author 陈磊
 * @Date 2019/12/27
 */
@RestController
public class DataSourceController {
    @Autowired
    DBChangeServiceImpl dbChangeService;
    @Autowired
    private DynamicDataSource dynamicDataSource;
    @RequestMapping("/changeDb")
    public String changeDb(@RequestBody(required = false) DatabaseSource data ) {
        if (data!=null){
            System.out.println(data);
            try {
                dbChangeService.changeDbByFrom(data);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }else {
            //切回主数据源
            DBContextHolder.clearDataSource();
            return "正在使用默认数据源！";
        }
        return "连接成功,当前数据源："+DBContextHolder.getDataSource();
    }

    @RequestMapping("/getDbInfo")
    public Object DbInfo() {
        String dataSource = DBContextHolder.getDataSource();
        Map<Object, Object> dynamicTargetDataSources = dynamicDataSource.getDynamicTargetDataSources();
        DruidDataSource druidDataSource = (DruidDataSource) dynamicTargetDataSources.get(dataSource);
        if (druidDataSource==null){
            return null;
        }
        HashMap<String, String> dbInfo = new HashMap<>();
        dbInfo.put("userName",druidDataSource.getUsername());
        dbInfo.put("dbUrl",dataSource);
        return dbInfo;
    }
}
