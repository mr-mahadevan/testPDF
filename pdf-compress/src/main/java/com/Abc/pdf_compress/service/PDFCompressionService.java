package com.Abc.pdf_compress.service;

import com.Abc.pdf_compress.utility.CompressionLevel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class PDFCompressionService {

    public File compressPDF(File inputFile, CompressionLevel compressionLevel) throws Exception {
        // Use try-with-resources to automatically close the PDDocument
        try (PDDocument document = PDDocument.load(inputFile)) {
            float scaleFactor = compressionLevel.getScaleFactor();

            // Iterate through pages and compress images
            for (PDPage page : document.getPages()) {
                for (var name : page.getResources().getXObjectNames()) {
                    if (page.getResources().isImageXObject(name)) {
                        PDImageXObject image = (PDImageXObject) page.getResources().getXObject(name);

                        BufferedImage originalImage = image.getImage();
                        int newWidth = Math.round(originalImage.getWidth() * scaleFactor);
                        int newHeight = Math.round(originalImage.getHeight() * scaleFactor);

                        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                        resizedImage.getGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);

                        File tempImageFile = File.createTempFile("compressed-", ".jpg");
                        ImageIO.write(resizedImage, "jpg", tempImageFile);

                        PDImageXObject compressedImage = PDImageXObject.createFromFile(tempImageFile.getAbsolutePath(), document);
                        page.getResources().put(name, compressedImage);

                        tempImageFile.delete();
                    }
                }
            }

            // Save the compressed PDF to a temporary file
            File compressedFile = File.createTempFile("compressed-", ".pdf");
            document.save(compressedFile);

            return compressedFile;
        } // PDDocument will automatically close here
    }
}
