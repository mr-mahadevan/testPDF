package com.Abc.pdf_compress.controller;

import com.Abc.pdf_compress.service.PDFCompressionService;
import com.Abc.pdf_compress.utility.CompressionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;


    @RestController
    @RequestMapping("/api/pdf")
    public class PDFCompressionController {

        @Autowired
        private PDFCompressionService pdfCompressionService;

        @PostMapping("/compress")
        public ResponseEntity<?> compressPDF(
                @RequestParam("file") MultipartFile file,
                @RequestParam("level") CompressionLevel level) {
            try {
                // Save the uploaded file temporarily
                File tempFile = File.createTempFile("uploaded-", ".pdf");
                file.transferTo(tempFile);

                // Compress the PDF
                File compressedFile = pdfCompressionService.compressPDF(tempFile, level);

                // Read the compressed file as a byte array
                byte[] fileContent = Files.readAllBytes(compressedFile.toPath());

                // Return the compressed file as a response
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "compressed.pdf");
                headers.setContentLength(fileContent.length);

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error compressing PDF: " + e.getMessage());
            }
        }
    }
