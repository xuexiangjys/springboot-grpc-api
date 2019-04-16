package com.xuexiang.springbootgrpcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootGrpcApiApplication {

    @Bean
    public GreeterService greeterService() {
        return new GreeterService();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGrpcApiApplication.class, args);
    }

}
