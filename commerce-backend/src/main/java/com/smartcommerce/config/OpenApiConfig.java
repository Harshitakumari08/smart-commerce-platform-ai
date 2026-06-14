package com.smartcommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI smartCommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Commerce Platform API")
                        .description("Enterprise commerce backend foundation with JWT, Redis, and Swagger")
                        .version("1.0.0"));
    }
}
