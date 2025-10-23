package com.example.demo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authenticationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authentication API")
                        .description("API for user authentication - login and registration")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Auth Team")
                                .email("contact@authapi.com")));
    }
}