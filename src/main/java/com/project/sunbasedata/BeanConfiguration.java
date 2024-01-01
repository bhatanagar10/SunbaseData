package com.project.sunbasedata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp")
                .build();
    }
}
