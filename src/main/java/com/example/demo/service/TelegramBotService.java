package com.example.demo.service;

import com.example.demo.config.TelegramProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class TelegramBotService extends TelegramLongPollingBot  {

    private final TelegramProperties telegramProperties;

    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(TelegramBotService.class);


    public TelegramBotService(TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;
    }


    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getName();
    }
    @Override
    public String getBotToken() {
        return telegramProperties.getToken();
    }

    public void sendPhotos(Long chatId, List<String> uris) throws TelegramApiException {
        SendMediaGroup mediaGroup = new SendMediaGroup();
        mediaGroup.setChatId(chatId);
        mediaGroup.setMedias(convertImageUrisToInputMedia(uris));
        execute(mediaGroup);
        log.info("Successfully sent several photo");
    }

    public void sendPhoto(Long chatId, List<String> uri) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(uri.get(0)));
        sendPhoto.setChatId(chatId);
        execute(sendPhoto);
        log.info("Successfully sent one photos");
    }

    private List<InputMedia> convertImageUrisToInputMedia(List<String> uris) {
        List<InputMedia> photosToUpload = new ArrayList<>();
        for (String photoUri: uris) {
            photosToUpload.add(new InputMediaPhoto(photoUri));
        }
        return photosToUpload;
    }
}
