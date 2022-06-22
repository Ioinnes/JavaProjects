package com.ioinnes.ru.springboot.testtask.processor;

import com.ioinnes.ru.springboot.testtask.controller.ImageController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// this logic will be here
@Component
public class ImageProcessor {
    private Logger logger = Logger.getLogger(ImageController.class.getSimpleName() + "::" + ImageProcessor.class.getSimpleName());
    @Value("${images.source}")
    private String catalog;

    public URI processUploadImage(MultipartFile multipartFile)  {
        Path dest;
        try {
            if (multipartFile == null ||
                    multipartFile.isEmpty() ||
                    !Objects.equals(multipartFile.getContentType(), MediaType.IMAGE_JPEG_VALUE) ||
                    multipartFile.getOriginalFilename() == null) {

                logger.log(Level.WARNING, "Illegal file");
                return null;
            }

            dest = Paths.get(catalog).resolve(multipartFile.getOriginalFilename());
            multipartFile.transferTo(dest.toFile());

        } catch (IOException e) {
            logger.log(Level.WARNING, "Something wrong with uploading image");
            return null;
        }

        return dest.toUri();
    }
}
