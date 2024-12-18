package com.example.demo.app.api.file;

import com.example.demo.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void create(MultipartFile file, String filenameWithoutExtension);
    File get(Long id);
    List<File> getAll();
    void delete(Long id);
}
