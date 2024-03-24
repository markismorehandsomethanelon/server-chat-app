package org.example.services;

import org.example.dtos.FileDataDTO;
import org.example.dtos.ResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public String storeFile(MultipartFile file);
    public ResponseDTO<FileDataDTO> getFile(String fileCode);
}
