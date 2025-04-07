package com.carddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class CardDemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CardDemoApplication.class);
        Environment env = app.run(args).getEnvironment();
        
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        
        String hostAddress = "localhost";
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String profiles = Arrays.toString(env.getActiveProfiles());
        
        System.out.println("----------------------------------------------------------");
        System.out.println("  Aplicación está ejecutándose! Acceso URLs:");
        System.out.println("  Perfiles activos: " + profiles);
        System.out.println("  Local: " + protocol + "://" + hostAddress + ":" + port + contextPath);
        System.out.println("  H2 Console: " + protocol + "://" + hostAddress + ":" + port + "/h2-console");
        System.out.println("  API Cards: " + protocol + "://" + hostAddress + ":" + port + "/api/cards");
        System.out.println("  API Transactions: " + protocol + "://" + hostAddress + ":" + port + "/api/transactions");
        System.out.println("----------------------------------------------------------");
    }
}