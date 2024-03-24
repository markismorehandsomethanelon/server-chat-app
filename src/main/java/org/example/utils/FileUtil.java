package org.example.utils;

import org.example.dtos.FileDataDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FileUtil {

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;

    @Value("${file.upload.path}")
    private String uploadPath;

    public FileDataDTO getFile(String fileCode) {
        try {
            Path filePath = Paths.get(uploadPath, fileCode);
            byte[] fileBytes = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            String fileName = filePath.getFileName().toString();

            // Get the file extension
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                extension = fileName.substring(dotIndex + 1);
            }

            return FileDataDTO.builder()
                    .data(fileBytes)
                    .extension(extension)
                    .contentType(contentType).build();

        } catch (Exception e) {
            return null;
        }
    }

    public String storeFile(FileDataDTO fileDataDTO){
        String fileName = UUID.randomUUID() + "." + fileDataDTO.getExtension();
        try {
            Path filePath = Paths.get(uploadPath).resolve(fileName);
            Files.write(filePath, fileDataDTO.getData());
            return fileName;
        } catch (IOException ex) {
            return null;
        }
    }

    public boolean deleteFile(String fileCode) {
        try {
            Path filePath = Paths.get(uploadPath, fileCode);
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            return false;
        }
    }
}
