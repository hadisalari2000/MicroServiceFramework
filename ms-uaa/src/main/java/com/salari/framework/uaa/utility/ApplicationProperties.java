package com.salari.framework.uaa.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(name = "properties", value = "classpath:/application.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@PropertySource(name = "messages", value = "classpath:/messages-application.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class ApplicationProperties {

    private static Environment environment;

    @Autowired
    ApplicationProperties(Environment environment) {
        ApplicationProperties.environment = environment;
    }

    public static String getProperty(String text) {
        return environment.getProperty(text);
    }
}
