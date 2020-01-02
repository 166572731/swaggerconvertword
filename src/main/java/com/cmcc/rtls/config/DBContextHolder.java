package com.cmcc.rtls.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : JCccc
 * @CreateTime : 2019/10/22
 * @Description :
 **/
public class DBContextHolder {
    private final static Logger log = LoggerFactory.getLogger(DBContextHolder.class);
    // 对当前线程的操作-线程安全的
    private static final ConcurrentHashMap<String,String> contextHolder = new ConcurrentHashMap<>();
 
    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        System.out.println("setDataSource:"+dataSource);
        contextHolder.put("dataKey",dataSource);
        log.info("已切换到数据源:{}",dataSource);
    }
 
    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get("dataKey");
    }
 
    // 删除数据源
    public static void clearDataSource() {
        contextHolder.clear();
        log.info("已切换到主数据源");
    }
 
}