package com.example.music.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageService {

    private final Path baseDir;

    public StorageService(@Value("${app.storage.base-dir:./uploaded-audio}") String baseDir) {
        this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create base storage directory", e);
        }
    }

    /**
     * Saves the incoming multipart file under a UUID filename preserving extension.
     * Returns the relative path (filename) that can be stored in DB.
     */
    public String saveFile(MultipartFile file) {
        try {
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String filename = UUID.randomUUID().toString() + ext;
            Path target = baseDir.resolve(filename);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            // return relative file name
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    /**
     * Returns a Path to the file (absolute). Caller must check existence.
     */
    public Path resolvePath(String relativeFilename) {
        return baseDir.resolve(relativeFilename).normalize();
    }

    /**
     * Read bytes (use for streaming / download controller)
     */
    public byte[] readAllBytes(String relativeFilename) {
        try {
            Path p = resolvePath(relativeFilename);
            return Files.readAllBytes(p);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public boolean exists(String relativeFilename) {
        return Files.exists(resolvePath(relativeFilename));
    }
}
