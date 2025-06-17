package com.example.factorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FactorialServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactorialServiceApplication.class, args);
    }
} 