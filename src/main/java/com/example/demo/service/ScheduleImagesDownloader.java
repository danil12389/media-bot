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


    @Scheduled(initialDelay = 2, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void scanNewImages() {
        var map =  ioDateCheckerService.getDomainsAndLastDate("date.txt");
        log.info("Checking new posts");
        map.forEach((key, value) -> {
            try {
            var newDate = vkImageParserService.getDateOfPost(key);
            if(Integer.parseInt(value) < newDate) {
                log.info("Download images from group {}", key);
                var imagesLinks = getBatchOfImagesLinks(key);

                if (imagesLinks == null) {
                    log.error("Image Links size is null");
                    return;
                }

                if (imagesLinks.size() < 2) {
                    telegramBotService.sendPhoto(telegramProperties.getChatId(), imagesLinks);
                } else {
                    telegramBotService.sendPhotos(telegramProperties.getChatId(), imagesLinks);
                }
                ioDateCheckerService.updateTimestamp("date.txt", key, String.valueOf(newDate));
                Thread.sleep(5000); // for avoid telegram 4xx error
            }
            }catch (Exception e) {
                log.error("Exception in scheduler, cause: {}", e.getMessage());
            }
        });

    }

    private List<String> getBatchOfImagesLinks(String groupName) {
        try {
            var getResponse =  vkImageParserService.getResponseEntityFromGroup(groupName);
            var imageUris = vkImageParserService.getImagesLinksFromVkRepsponseQuery(getResponse);
            List<String> res = new ArrayList<>();
            for (URI uri : imageUris) {
                res.add(uri.toString());
            }
             return res;
        }catch (Exception e) {
            log.error("Fail to download images from VK, cause: {} ", e.getMessage());
        }
        return null;
    }
}
