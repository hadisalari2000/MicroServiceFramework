package com.salari.framework.msuaa;

import com.salari.framework.common.handler.exception.RestTemplateException;
import com.salari.framework.msuaa.utility.RibbonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
@ComponentScan(basePackages = {"com.salari.framework.common", "com.salari.framework.msuaa"})
@RibbonClient(name = "server", configuration = RibbonConfiguration.class)
public class MsUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUaaApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(new RestTemplateException()).build();
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(MsUaaApplication.class);
    }

}
