package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author raven
 * @Description 
 * @Date  19:13
 * @Param 
 * @return 
 **/
@SpringBootApplication
@ComponentScan(value = {"com.*", "init","correspond"})
@EntityScan("com.web.pojo")
@EnableJpaRepositories("com.web.dao")
@EnableSwagger2
public class SmartApplication {

    public static void main(String[] args)  {
        // http://localhost/swagger-ui.html

        SpringApplication.run(SmartApplication.class, args);
    }

}
