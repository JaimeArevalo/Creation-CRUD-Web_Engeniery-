package com.carddemo.service;

import com.carddemo.exception.BackupException;
import com.carddemo.model.Card;
import com.carddemo.repository.CardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BackupService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BACKUP_DIR = "backups";
    private static final int RETENTION_DAYS = 7;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public String performBackup() {
        try {
            // Create backup directory if it doesn't exist
            Path backupDir = Paths.get(BACKUP_DIR);
            Files.createDirectories(backupDir);

            // Generate backup filename
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String filename = String.format("backup_%s.json", timestamp);
            Path backupFile = backupDir.resolve(filename);

            // Get all cards and write to file
            List<Card> cards = cardRepository.findAll();
            objectMapper.writeValue(backupFile.toFile(), cards);

            // Clean old backups
            cleanOldBackups();

            return backupFile.toString();
        } catch (Exception e) {
            throw new BackupException("Failed to create backup: " + e.getMessage());
        }
    }

    private void cleanOldBackups() {
        try {
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                return;
            }

            List<Path> backupFiles = Files.list(backupDir)
                .filter(path -> path.toString().endsWith(".json"))
                .sorted((p1, p2) -> -p1.toString().compareTo(p2.toString()))
                .collect(Collectors.toList());

            if (backupFiles.size() > RETENTION_DAYS) {
                for (int i = RETENTION_DAYS; i < backupFiles.size(); i++) {
                    Files.delete(backupFiles.get(i));
                }
            }
        } catch (Exception e) {
            throw new BackupException("Failed to clean old backups: " + e.getMessage());
        }
    }

    public List<String> listBackups() {
        try {
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                return List.of();
            }

            return Files.list(backupDir)
                .filter(path -> path.toString().endsWith(".json"))
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BackupException("Failed to list backups: " + e.getMessage());
        }
    }
}