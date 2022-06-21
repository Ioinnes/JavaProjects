package com.ioinnes.ru.springboot.testtask.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final Logger logger = Logger.getLogger(ImageController.class.getSimpleName());
    @Value("${images.source}")
    private String catalog;

    @PostMapping("/upload")
    public URI uploadImage(@RequestParam("image") MultipartFile multipartFile)  {
        Path dest = null;
        try {
            if (multipartFile == null || multipartFile.isEmpty() || !Objects.equals(multipartFile.getContentType(), MediaType.IMAGE_JPEG_VALUE))
                throw new IllegalArgumentException();
            dest = Paths.get(catalog).resolve(multipartFile.getName());
            multipartFile.transferTo(dest.toFile());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Something wrong with uploading image");
        }
        catch (IllegalArgumentException e) {
            logger.log(Level.INFO, "Wrong format ot file is empty");
        }

        return dest == null ? null : dest.toUri();
    }
}
