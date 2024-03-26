package org.example.controllers;

import org.example.dtos.FileDataDTO;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1")
public class FileDownloadController {
    @Autowired
    private FileUtil fileUtil;

    @GetMapping("/files/downloadFile/{fileCode}")
    public ResponseEntity<FileDataDTO> downloadFile(@PathVariable String fileCode) throws IOException {
        FileDataDTO fileDataDTO = fileUtil.getFile(fileCode);

        if (fileDataDTO == null){
            return ResponseEntity.badRequest().body(fileDataDTO);
        }

        return ResponseEntity.ok(fileDataDTO);
    }

    @GetMapping("/files/fetch/{fileCode}")
    public ResponseEntity<byte[]> fetchFile(@PathVariable String fileCode) throws IOException {
        FileDataDTO fileDataDTO = fileUtil.getFile(fileCode);

        if (fileDataDTO == null){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(fileDataDTO.getData());
    }

}
