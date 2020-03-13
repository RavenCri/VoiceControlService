package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author raven
 * @Description 
 * @Date  19:13
 * @Param 
 * @return 
 **/
@SpringBootApplication
@ComponentScan(value = {"controller","config","server","correspond"})
@EntityScan("pojo")
@EnableJpaRepositories("dao")
public class SmartApplication {

    public static void main(String[] args)  {


        SpringApplication.run(SmartApplication.class, args);
    }

}
