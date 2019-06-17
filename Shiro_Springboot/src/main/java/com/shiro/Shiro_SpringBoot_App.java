package com.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.shiro.dao")
public class Shiro_SpringBoot_App {
    public static void main( String[] args ){
        SpringApplication.run(Shiro_SpringBoot_App.class, args);
    }
}
