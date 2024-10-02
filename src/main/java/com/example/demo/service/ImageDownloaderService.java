package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageDownloaderService {


    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(ImageDownloaderService.class);


    public List<BufferedImage> downloadAndSaveImages(List<URI> images) throws IOException {
        List<BufferedImage> output = new ArrayList<>();
        try {
            for (URI image : images) {
                output.add(ImageIO.read(image.toURL()));
            }
            return output;
        }catch (Exception e) {
            log.error("Can't download image, error: {}", e.getMessage());
        }

        return null;
    }
}
