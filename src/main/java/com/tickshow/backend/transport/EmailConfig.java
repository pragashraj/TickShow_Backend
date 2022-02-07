package com.tickshow.backend.transport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Bean
    public freemarker.template.Configuration configuration() {
        return new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_20);
    }
}