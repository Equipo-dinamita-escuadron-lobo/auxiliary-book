package com.unicauca.edu.co.auxiliary_book.infrastructure.out.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.domain.ports.storage.IFileStoragePort;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileSystemStorageAdapter implements IFileStoragePort {

    private final Path basePath;

    public FileSystemStorageAdapter(@Value("${storage.scheduled.base-path:./storage/scheduled-tasks}") String basePath) {
        this.basePath = Path.of(basePath).toAbsolutePath().normalize();
    }

    @Override
    public String store(byte[] content, String fileName) {
        try {
            Files.createDirectories(basePath);
            long timestamp = Instant.now().toEpochMilli();
            String sanitizedName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path target = basePath.resolve(timestamp + "_" + sanitizedName);
            Files.write(target, content);
            log.info("Stored artifact at {}", target);
            return target.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to store scheduled artifact", ex);
        }
    }
}
