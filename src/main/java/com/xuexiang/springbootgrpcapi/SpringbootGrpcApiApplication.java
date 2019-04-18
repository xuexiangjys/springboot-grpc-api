package com.xuexiang.springbootgrpcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xuexiang.springbootgrpcapi.mapper")
public class SpringbootGrpcApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGrpcApiApplication.class, args);
    }

}
