package dev.cyberser.labj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pdf")
public class PDFController {
    @Autowired
    private PDFRepository pdfRepository;

    // PDFS ENDPOINTS
    @GetMapping("/all")
    public List<PDF> getPdfs() {
        return pdfRepository.findAll();
    }

    // USER ENDPOINTS
    @PostMapping(value = "/post", consumes = "application/json")
    public PDF createPdf(@Valid @RequestBody PDF pdf) {
        return pdfRepository.save(pdf);
    }

    @PutMapping("/update/{id}")
    public PDF updatePdf(@PathVariable Long id, @Valid @RequestBody PDF pdfDetails) {
        PDF pdf = pdfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PDF", "id", id));
        pdf.setAuthor(pdfDetails.getAuthor());
        pdf.setContent(pdfDetails.getContent());
        return pdfRepository.save(pdf);
    }
    
    @GetMapping("/get/{id}")
    public PDF getPdfById(@PathVariable Long id) {
        return pdfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PDF", "id", id));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePdf(@PathVariable Long id) {
        PDF pdf = pdfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PDF", "id", id));
        pdfRepository.delete(pdf);
        return ResponseEntity.ok().build();
    }
}
