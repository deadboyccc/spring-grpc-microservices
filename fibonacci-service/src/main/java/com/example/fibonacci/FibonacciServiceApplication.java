package com.example.fibonacci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FibonacciServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FibonacciServiceApplication.class, args);
    }
} 