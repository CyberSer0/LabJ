package dev.cyberser.labj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private PDFRepository pdfRepository;

    private static final String UPLOAD_DIR = "/pdf/uploads";

    public String storeFile(MultipartFile file, Long authorId) {
        String fileName = authorId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(UPLOAD_DIR + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Failed to store file: " + fileName, e);
        }
    }


    public PDF getFileById(Long id) {
        return pdfRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PDF", "id", id));
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}
