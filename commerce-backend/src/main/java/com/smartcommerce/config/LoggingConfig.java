package com.smartcommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.LoggerFactory;

@Configuration
public class LoggingConfig {
    @Bean
    public org.slf4j.Logger logger() {
        return LoggerFactory.getLogger("SMART_COMMERCE");
    }
}
