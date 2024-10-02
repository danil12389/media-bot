package com.example.demo.service;

import com.example.demo.config.TelegramProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Service
public class ScheduleImagesDownloader {

    private final VkImageParserService vkImageParserService;
    private final ImageDownloaderService imageDownloaderService;
    private final IODateCheckerService ioDateCheckerService;
    private final TelegramBotService telegramBotService;
    private final TelegramProperties telegramProperties;
    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(ScheduleImagesDownloader.class);

    public ScheduleImagesDownloader(VkImageParserService vkImageParserService, ImageDownloaderService imageDownloaderService, IODateCheckerService ioDateCheckerService, TelegramBotService telegramBotService, TelegramProperties telegramProperties) {
        this.vkImageParserService = vkImageParserService;
        this.imageDownloaderService = imageDownloaderService;
        this.ioDateCheckerService = ioDateCheckerService;
        this.telegramBotService = telegramBotService;
        this.telegramProperties = telegramProperties;
    }


    @Scheduled(initialDelay = 2, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void scanNewImages() {
        var map =  ioDateCheckerService.getDomainsAndLastDate("date.txt");
        log.info("Checking new posts");
        map.forEach((key, value) -> {
            var newDate = vkImageParserService.getDateOfPost(key);
            if(Integer.valueOf(value) < vkImageParserService.getDateOfPost(key)) {
                log.info("Download images from group {}", key);
                var getResponse =  vkImageParserService.getResponseEntityFromGroup(key);
                var imageUris = vkImageParserService.getImagesLinksFromVkRepsponseQuery(getResponse);
                List<String> res = new ArrayList<>();
                for (URI uri : imageUris) {
                    res.add(uri.toString());
                }
                if (imageUris.size() < 2) {
                    telegramBotService.sendPhoto(telegramProperties.getChatId(), res);
                }else {

                    telegramBotService.sendPhotos(telegramProperties.getChatId(), res);
                }
                ioDateCheckerService.updateTimestamp("date.txt", key, String.valueOf(newDate));
            }
        });

    }

//    private List<BufferedImage> getImages() {
//        var getResponse =  vkImageParserService.getResponseEntityFromGroup("aacontent");
//        var imageUris = vkImageParserService.getImagesLinksFromVkRepsponseQuery(getResponse);
//        try {
//          return imageDownloaderService.downloadAndSaveImages(imageUris);
//        }catch (Exception e) {
//            System.out.println("Exception was caught, " + e.getMessage());
//        }
//        return null;
//    }
}
