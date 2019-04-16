package com.xuexiang.springbootgrpcapi;

import com.xuexiang.springbootgrpcapi.grpc.GreeterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xuexiang.springbootgrpcapi.mapper")
public class SpringbootGrpcApiApplication {

    @Bean
    public GreeterService greeterService() {
        return new GreeterService();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGrpcApiApplication.class, args);
    }

}
