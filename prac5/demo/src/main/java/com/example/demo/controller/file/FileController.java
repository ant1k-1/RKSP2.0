package com.example.demo.controller.file;

import com.example.demo.app.api.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Controller
public class FileController {
    private final FileService fileService;

    @GetMapping("/ping")
    ResponseEntity<?> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/")
    ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(fileService.getAll());
    }

    @GetMapping("/{id}/download")
    ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.get(id).getFileData());
    }

    @PostMapping("/upload")
    void uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("filename") String filenameWithoutExtension)
    {
        fileService.create(file, filenameWithoutExtension);
    }

    @DeleteMapping("/{id}/delete")
    void deleteFile(@PathVariable Long id) {
        fileService.delete(id);
    }
}
