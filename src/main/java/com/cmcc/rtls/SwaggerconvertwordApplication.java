package com.cmcc.rtls;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gitee.sunchenbin.mybatis.actable.manager.*")
@MapperScan("com.gitee.sunchenbin.mybatis.actable.dao.*")
@ComponentScan("com.cmcc.rtls.controller")
public class SwaggerconvertwordApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerconvertwordApplication.class, args);
    }
}
