package com.example.excalibur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.example")
@EnableJpaRepositories("com.example.repositories")
@EntityScan("com.example.models")
public class ExcaliburApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcaliburApplication.class, args);
    }

}
