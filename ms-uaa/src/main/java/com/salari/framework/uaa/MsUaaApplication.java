package com.salari.framework.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsUaaApplication {

    /*@Bean
    public Logger logger() {
        return LoggerFactory.getLogger(MsUaaApplication.class);
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(MsUaaApplication.class, args);
    }
}
