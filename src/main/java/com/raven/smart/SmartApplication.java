package com.raven.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author raven
 * @Description 
 * @Date  19:13
 * @Param 
 * @return 
 **/
@SpringBootApplication
@ComponentScan(value = {"controller","config"})
public class SmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartApplication.class, args);
    }

}
