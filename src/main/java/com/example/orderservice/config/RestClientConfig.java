package com.example.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${stock.service.url:http://localhost:8081/api/products}")
    private String stockServiceUrl;

    @Bean
    public RestClient stockServiceClient() {
        return RestClient.builder()
                .baseUrl(stockServiceUrl)
                .build();
    }
}