package com.salari.framework.msuaa;

import com.salari.framework.msuaa.handler.exception.RestTemplateException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
public class MsUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUaaApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder
                /*.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(10))*/
                .errorHandler(new RestTemplateException())
                .build();
    }
}
