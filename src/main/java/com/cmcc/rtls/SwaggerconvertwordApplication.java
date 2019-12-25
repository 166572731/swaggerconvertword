package com.cmcc.rtls;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("com/cmcc/rtls/mapper")
public class SwaggerconvertwordApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerconvertwordApplication.class, args);
    }
}
