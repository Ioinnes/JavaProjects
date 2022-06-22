package com.ioinnes.ru.springboot.testtask.controller;

import com.ioinnes.ru.springboot.testtask.processor.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageProcessor imageProcessor;

    @PostMapping("/upload")
    public CompletableFuture<URI> uploadMessage(@RequestParam("image") MultipartFile multipartFile) {
        return CompletableFuture.supplyAsync(() -> imageProcessor.processUploadImage(multipartFile));
    }
}
