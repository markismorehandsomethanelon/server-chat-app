package org.example.services.impl;

import org.example.dtos.FileDataDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public String storeFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extensionPart = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String fileName = UUID.randomUUID().toString() + "." + extensionPart;

        try {
            Path filePath = Paths.get(uploadPath).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public ResponseDTO<FileDataDTO> getFile(String fileCode) {
        try {
            Path filePath = Paths.get(uploadPath, fileCode);
            byte[] fileBytes = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);

            FileDataDTO fileDataDTO = FileDataDTO.builder()
                    .data(fileBytes)
                    .contentType(contentType).build();

            return ResponseDTO.<FileDataDTO>builder()
                    .success(true)
                    .data(fileDataDTO)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<FileDataDTO>builder()
                    .success(false)
                    .message("Cannot get file data")
                    .build();
        }
    }
}
