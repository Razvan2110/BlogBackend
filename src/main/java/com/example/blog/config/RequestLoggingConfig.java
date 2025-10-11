package com.example.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);     // afișează parametrii din URL
        filter.setIncludePayload(true);         // afișează corpul cererii (JSON)
        filter.setIncludeHeaders(false);        // poți pune true dacă vrei header-ele
        filter.setMaxPayloadLength(10000);      // câte caractere din payload să fie afișate
        filter.setAfterMessagePrefix("REQUEST DATA : "); // prefix pentru fiecare log
        return filter;
    }
}
