package com.salari.framework.gatewayserver;

import com.salari.framework.common.handler.exception.RestTemplateException;
import com.salari.framework.gatewayserver.filters.RequestFilter;
import com.salari.framework.gatewayserver.filters.ResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan(basePackages = {"com.salari.framework.common", "com.salari.framework.gatewayserver"})
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RequestFilter authorizationFilter() {
        return new RequestFilter();
    }

    @Bean
    public ResponseFilter postFilter() {
        return new ResponseFilter();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(new RestTemplateException()).build();
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(GatewayServerApplication.class);
    }


}
