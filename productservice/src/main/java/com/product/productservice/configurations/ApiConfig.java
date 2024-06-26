package com.product.productservice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    @Value("${fake.store.api.url}")
    private String fakeStoreApiUrl;

    public String getFakeStoreApiUrl() {
        return fakeStoreApiUrl;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

