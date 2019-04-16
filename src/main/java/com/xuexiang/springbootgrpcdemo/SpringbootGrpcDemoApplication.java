package com.xuexiang.springbootgrpcdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootGrpcDemoApplication {

    @Bean
    public GreeterService greeterService() {
        return new GreeterService();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGrpcDemoApplication.class, args);
    }

}
