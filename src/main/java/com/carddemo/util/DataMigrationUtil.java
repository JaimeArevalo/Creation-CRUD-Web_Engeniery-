package com.carddemo.util;

import com.carddemo.model.Card;
import com.carddemo.model.Transaction;
import com.carddemo.repository.CardRepository;
import com.carddemo.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
@Profile({"dev", "prod"})
public class DataMigrationUtil implements CommandLineRunner {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private static final String EXPORT_FILE = "data-migration.json";

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("export")) {
            exportToJson();
        } else if (args.length > 0 && args[0].equalsIgnoreCase("import")) {
            importFromJson();
        }
    }

    public void exportToJson() throws Exception {
        List<Card> cards = cardRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(EXPORT_FILE), Map.of(
                "cards", cards,
                "transactions", transactions
        ));
        System.out.println("Datos exportados a " + EXPORT_FILE);
    }

    public void importFromJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> data = mapper.readValue(new File(EXPORT_FILE), Map.class);
        List<Map<String, Object>> cards = (List<Map<String, Object>>) data.get("cards");
        List<Map<String, Object>> transactions = (List<Map<String, Object>>) data.get("transactions");
        // Limpia la base antes de importar
        transactionRepository.deleteAll();
        cardRepository.deleteAll();
        // Importa tarjetas
        for (Map<String, Object> c : cards) {
            Card card = mapper.convertValue(c, Card.class);
            cardRepository.save(card);
        }
        // Importa transacciones
        for (Map<String, Object> t : transactions) {
            Transaction tx = mapper.convertValue(t, Transaction.class);
            transactionRepository.save(tx);
        }
        System.out.println("Datos importados desde " + EXPORT_FILE);
    }
} 