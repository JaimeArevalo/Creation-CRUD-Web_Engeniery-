package com.carddemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public CommandLineRunner databaseConnectionCheck(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("\n=== Database Connection Check ===");
            System.out.println("Active Profiles: " + Arrays.toString(env.getActiveProfiles()));
            System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
            System.out.println("Database Username: " + env.getProperty("spring.datasource.username"));
            System.out.println("Database Driver: " + env.getProperty("spring.datasource.driver-class-name"));
            
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("\n=== Connection Details ===");
                System.out.println("Connection successful!");
                System.out.println("Driver: " + connection.getMetaData().getDriverName());
                System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + connection.getMetaData().getDatabaseProductVersion());
                System.out.println("URL: " + connection.getMetaData().getURL());
                System.out.println("Username: " + connection.getMetaData().getUserName());
                
                // Test query
                System.out.println("\n=== Testing Query ===");
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                System.out.println("Query test successful!");
                
            } catch (SQLException e) {
                System.err.println("\n=== Database Connection Error ===");
                System.err.println("Error Type: " + e.getClass().getName());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Message: " + e.getMessage());
                System.err.println("Stack Trace:");
                e.printStackTrace();
                throw new RuntimeException("Database connection failed", e);
            }
        };
    }
} 