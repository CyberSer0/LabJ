package dev.cyberser.labj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    private final PDFRepository pdfRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public PDFController(PDFRepository pdfRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.pdfRepository = pdfRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/all")
    public List<PDF> getPdfs() {
        return pdfRepository.findAll();
    }

    @PostMapping(value = "/post")
    public String createPdf(@RequestParam("file") MultipartFile file, @RequestParam("authorId") Long authorId) throws IOException {
        User author = getUserById(authorId);
        return fileStorageService.storeFile(file, authorId);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPDF(@RequestParam("file") MultipartFile file, @RequestParam("authorId") Long authorId) {
        String filePath = fileStorageService.storeFile(file, authorId);

        // Optionally, you can return the stored file path or any success message
        return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully. Path: " + filePath);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable Long id) {
        PDF pdf = fileStorageService.getFileById(id);
        ByteArrayResource resource = new ByteArrayResource(pdf.getContent());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getFileName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.getContent().length)
                .body(resource);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
