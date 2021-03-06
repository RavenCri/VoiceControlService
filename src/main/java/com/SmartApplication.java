package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * @Author raven
 * @Description 
 * @Date  19:13
 * @Param 
 * @return 
 **/
@SpringBootApplication

@EntityScan("com.web.pojo")
@EnableJpaRepositories("com.web.dao")
@EnableSwagger2
@EnableScheduling
@EnableConfigurationProperties

public class SmartApplication {

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        // http://localhost:8080/swagger-ui.html

        SpringApplication.run(SmartApplication.class, args);
    }



}
