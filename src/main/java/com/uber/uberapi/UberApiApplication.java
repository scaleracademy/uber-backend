package com.uber.uberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UberApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberApiApplication.class, args);
    }

}
