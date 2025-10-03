package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI movieRankerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Ranker API")
                        .description("A comprehensive API for managing and ranking movies")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Movie Ranker Team")
                                .email("contact@movieranker.com")));
    }
}