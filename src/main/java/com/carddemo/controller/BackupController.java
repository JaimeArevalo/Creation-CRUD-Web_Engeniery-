package com.carddemo.controller;

import com.carddemo.model.dto.BackupResponse;
import com.carddemo.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @PostMapping("/manual")
    public ResponseEntity<BackupResponse> createManualBackup() {
        try {
            String backupFile = backupService.performBackup();
            BackupResponse response = new BackupResponse();
            response.setFilename(backupFile);
            response.setTimestamp(LocalDateTime.now());
            response.setStatus("SUCCESS");
            response.setMessage("Backup created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BackupResponse response = new BackupResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setStatus("ERROR");
            response.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<BackupResponse>> listBackups() {
        try {
            List<BackupResponse> backups = backupService.listBackups().stream()
                .map(filename -> {
                    BackupResponse response = new BackupResponse();
                    response.setFilename(filename);
                    response.setStatus("AVAILABLE");
                    return response;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(backups);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}