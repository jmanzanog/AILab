package com.jmanzanog.ailab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for AILab.
 * This application integrates with LangChain4j to provide AI capabilities.
 */
@SpringBootApplication
public class AILabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AILabApplication.class, args);
    }
}