package com.salari.framework.uaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsUaaApplication {
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(MsUaaApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MsUaaApplication.class, args);
    }
}
