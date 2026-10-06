package com.nhnacademy.mcp.config;

import com.nhnacademy.mcp.properties.NaruProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(RestClient.Builder builder, NaruProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }
}
