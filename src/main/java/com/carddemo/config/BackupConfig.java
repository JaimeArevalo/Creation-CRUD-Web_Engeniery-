package com.carddemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BackupConfig {
    @Value("${backup.directory:backups}")
    private String backupDirectory;

    @Value("${backup.retention-days:7}")
    private int retentionDays;

    public String getBackupDirectory() {
        return backupDirectory;
    }

    public int getRetentionDays() {
        return retentionDays;
    }
}