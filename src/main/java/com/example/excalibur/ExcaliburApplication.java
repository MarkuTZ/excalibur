package com.example.excalibur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class ExcaliburApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcaliburApplication.class, args);
    }

}
