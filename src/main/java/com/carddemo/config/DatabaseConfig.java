package com.carddemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    @Profile("prod")
    public CommandLineRunner databaseConnectionCheck(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Database connection successful!");
                System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
                System.out.println("Database Username: " + env.getProperty("spring.datasource.username"));
                System.out.println("Database Driver: " + connection.getMetaData().getDriverName());
                System.out.println("Database Product: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
            } catch (SQLException e) {
                System.err.println("Database connection failed!");
                System.err.println("Error: " + e.getMessage());
                throw e;
            }
        };
    }
} 