package com.carddemo.config;

import com.carddemo.model.Card;
import com.carddemo.model.Transaction;
import com.carddemo.repository.CardRepository;
import com.carddemo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final Random random = new Random();
    private final String[] names = {"John Smith", "Emma Johnson", "Michael Brown", "Olivia Davis", "William Wilson", 
                                   "Sophia Martinez", "James Taylor", "Isabella Anderson", "Benjamin Thomas", "Mia Jackson"};
    private final String[] descriptions = {"Online purchase", "Restaurant payment", "Grocery shopping", "Gas station", 
                                          "Utility bill", "Subscription fee", "Mobile payment", "Electronics store", 
                                          "Clothing purchase", "Travel booking"};
    private final String[] merchants = {"Amazon", "Walmart", "Target", "Best Buy", "Costco", "Home Depot", "Starbucks", 
                                       "McDonalds", "Netflix", "Apple Store"};

    @Override
    public void run(String... args) {
        // First check if data already exists
        if (cardRepository.count() > 0) {
            System.out.println("Database already contains data, skipping initialization");
            return;
        }

        System.out.println("Starting data initialization...");
        
        List<Card> cards = createCards(50);  // Create 50 cards
        List<Transaction> transactions = createTransactions(cards, 1000);  // Create 1000 transactions
        
        System.out.println("Data initialization completed");
    }

    private List<Card> createCards(int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Card card = new Card();
            card.setCardHolder(names[random.nextInt(names.length)]);
            card.setCardNumber(generateCardNumber());
            card.setExpirationDate(generateExpirationDate());
            card.setCvv(generateCVV());
            card.setCreditLimit(1000.0 + random.nextInt(9000));
            card.setBalance(0.0);
            card.setStatus("ACTIVE");
            card.setIsActive(true);
            cards.add(cardRepository.save(card));
        }
        System.out.println("Created " + cards.size() + " cards");
        return cards;
    }

    private List<Transaction> createTransactions(List<Card> cards, int count) {
        List<Transaction> transactions = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.now().minusMonths(6);
        
        for (int i = 0; i < count; i++) {
            Card card = cards.get(random.nextInt(cards.size()));
            String type = random.nextBoolean() ? "PURCHASE" : "PAYMENT";
            
            Transaction transaction = new Transaction();
            transaction.setCard(card);
            
            BigDecimal amount;
            if ("PURCHASE".equals(type)) {
                amount = new BigDecimal(10 + random.nextInt(490));
                card.setBalance(card.getBalance() + amount.doubleValue());
            } else {
                double maxPayment = Math.min(card.getBalance(), 500.0);
                if (maxPayment <= 0) {
                    maxPayment = 10.0;  // Minimum payment
                }
                amount = new BigDecimal(Math.max(10, random.nextInt((int)maxPayment)));
                card.setBalance(Math.max(0, card.getBalance() - amount.doubleValue()));
            }
            
            transaction.setAmount(amount);
            transaction.setType(type);
            
            String merchant = merchants[random.nextInt(merchants.length)];
            String desc = descriptions[random.nextInt(descriptions.length)];
            transaction.setDescription(desc + " at " + merchant);
            
            // Set random date within last 6 months
            LocalDateTime transactionDate = startDate.plusSeconds(random.nextInt(15552000)); // 6 months in seconds
            transaction.setTransactionDate(transactionDate);
            
            cardRepository.save(card);  // Update card balance
            transactions.add(transactionRepository.save(transaction));
            
            if (i % 100 == 0) {
                System.out.println("Created " + i + " transactions");
            }
        }
        System.out.println("Created " + transactions.size() + " transactions");
        return transactions;
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateExpirationDate() {
        int month = 1 + random.nextInt(12);
        int year = 25 + random.nextInt(5);
        return String.format("%02d/%02d", month, year);
    }

    private String generateCVV() {
        return String.format("%03d", random.nextInt(1000));
    }
}