package org.example.coindesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class CoindeskApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoindeskApplication.class, args);
    }
}