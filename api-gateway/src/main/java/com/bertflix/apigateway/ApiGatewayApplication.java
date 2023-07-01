package com.bertflix.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ApiGatewayApplication {
    public static void main(String[] args){
        System.out.println("STARTING GATEWAY");
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
