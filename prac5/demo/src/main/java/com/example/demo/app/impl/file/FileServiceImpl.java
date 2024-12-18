package com.example.demo.app.impl.file;

import com.example.demo.app.api.exception.FileNotFoundException;
import com.example.demo.app.api.file.FileService;
import com.example.demo.domain.File;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    public void create(MultipartFile file, String filenameWithoutExtension) {
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileExtension =
                    originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = filenameWithoutExtension + fileExtension;
                File newFile = new File();
                newFile.setFileName(newFilename);
                newFile.setFileData(file.getBytes());
                newFile.setFileSize(file.getSize());
                fileRepository.save(newFile);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public File get(Long id) {
        return fileRepository.findById(id).orElseThrow(
            () -> new FileNotFoundException(id)
        );
    }

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        fileRepository.deleteById(id);
    }
}
