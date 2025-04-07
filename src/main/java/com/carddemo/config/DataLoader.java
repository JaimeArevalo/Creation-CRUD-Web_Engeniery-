package com.carddemo.config;

import com.carddemo.model.Card;
import com.carddemo.model.Transaction;
import com.carddemo.repository.CardRepository;
import com.carddemo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Carga datos de ejemplo solo en entorno de desarrollo
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner loadDevData() {
        return args -> {
            System.out.println("Cargando datos para entorno de DESARROLLO");
            loadSampleData(20, "DEV");
        };
    }

    /**
     * Carga un conjunto mínimo de datos para pruebas
     */
    @Bean
    @Profile("test")
    public CommandLineRunner loadTestData() {
        return args -> {
            System.out.println("Cargando datos para entorno de PRUEBAS");
            loadSampleData(5, "TEST");
        };
    }

    /**
     * Carga datos solo si la tabla de tarjetas está vacía (producción)
     */
    @Bean
    @Profile("prod")
    public CommandLineRunner loadProdData() {
        return args -> {
            if (cardRepository.count() == 0) {
                System.out.println("Base de datos de PRODUCCIÓN vacía. Cargando datos iniciales...");
                // En producción solo creamos una tarjeta demo si no hay datos
                Card card = new Card();
                card.setCardNumber("1111222233334444");
                card.setCardHolder("DEMO CARD");
                card.setExpirationDate("12/30");
                card.setCvv("123");
                card.setCreditLimit(1000.0);
                card.setBalance(0.0);
                card.setStatus("ACTIVE");
                card.setIsActive(true);
                
                cardRepository.save(card);
            } else {
                System.out.println("Base de datos de PRODUCCIÓN ya contiene datos. Omitiendo carga inicial.");
            }
        };
    }

    /**
     * Método auxiliar para cargar datos de ejemplo
     */
    private void loadSampleData(int count, String environment) {
        // Crear tarjetas de ejemplo
        for (int i = 1; i <= count; i++) {
            Card card = new Card();
            card.setCardNumber(String.format("%016d", i));
            
            // Usando solo letras para cumplir con la validación
            card.setCardHolder("User " + environment + " " + convertToLetters(i));
            
            card.setExpirationDate("12/" + (25 + (i % 5)));
            card.setCvv("" + (100 + i));
            card.setCreditLimit(1000.0 * i);
            card.setBalance(i * 100.0);
            card.setStatus("ACTIVE");
            card.setIsActive(true);
            
            Card savedCard = cardRepository.save(card);
            
            // Crear algunas transacciones para cada tarjeta
            for (int j = 1; j <= 3; j++) {
                Transaction purchase = new Transaction();
                purchase.setCard(savedCard);
                purchase.setAmount(new BigDecimal(j * 50.0));
                purchase.setType("PURCHASE");
                purchase.setDescription("Compra de prueba " + j + " para tarjeta " + i);
                purchase.setTransactionDate(LocalDateTime.now().minusDays(j));
                
                transactionRepository.save(purchase);
                
                if (j % 2 == 0) {
                    Transaction payment = new Transaction();
                    payment.setCard(savedCard);
                    payment.setAmount(new BigDecimal(j * 30.0));
                    payment.setType("PAYMENT");
                    payment.setDescription("Pago de prueba " + j + " para tarjeta " + i);
                    payment.setTransactionDate(LocalDateTime.now().minusDays(j - 1));
                    
                    transactionRepository.save(payment);
                }
            }
        }
    }
    
    /**
     * Convierte un número en una representación de letras
     * Por ejemplo: 1 -> "One", 2 -> "Two", etc.
     */
    private String convertToLetters(int number) {
        String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", 
                         "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", 
                         "Eighteen", "Nineteen"};
        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        
        if (number < 20) {
            return units[number];
        } else {
            int unit = number % 10;
            int ten = number / 10;
            return tens[ten] + (unit > 0 ? units[unit] : "");
        }
    }
}