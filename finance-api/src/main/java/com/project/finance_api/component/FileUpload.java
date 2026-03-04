package com.project.finance_api.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUpload {

    @Value("${server.url:http://localhost:8080}")
    private String serverUrl;

    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("No file selected.");
        }

        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "src/main/resources/uploads" + File.separator;

        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directories.");
        }

        String originalFileName = file.getOriginalFilename();
        String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        String uniqueFileName = UUID.randomUUID() + "_" + safeFileName;

        Path filePath = Paths.get(uploadDir + uniqueFileName);
        file.transferTo(filePath.toFile());

        return serverUrl + "/api/media/" + uniqueFileName;
    }
}
