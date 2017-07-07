package com.engagetech.expenses.rest;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class RestConfig {
    public static final String APP_PATH = "/app";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.simpleDateFormat(DATE_FORMAT);
        b.timeZone(TimeZone.getTimeZone("UTC"));
        return b;
    }
}