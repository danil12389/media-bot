package com.example.demo.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Image;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VkImageParserService {

    private final TransportClient transportClient;
    private final VkApiClient vkApiClient;

    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(VkImageParserService.class);

    @Autowired
    private ServiceActor testAppServiceActor;

    public VkImageParserService( ) {
        this.transportClient = new HttpTransportClient();
        this.vkApiClient = new VkApiClient(this.transportClient);
    }

    public GetResponse getResponseEntityFromGroup(String groupDomain) {
        try {
           return vkApiClient.wall().get(testAppServiceActor).domain(groupDomain).offset(1).execute();
        } catch (Exception e) {
            log.info("Exception was thrown: {}", e.getMessage());
        }
        return null;
    }

    public Integer getDateOfPost(String groupDomain) {
        var resp = getResponseEntityFromGroup(groupDomain);
        var attaches = resp.getItems().get(1).getAttachments();
        for (int i = 0; i < attaches.size(); i++) {
            if(attaches.get(i).getType().equals(WallpostAttachmentType.PHOTO)) {
                return attaches.get(i).getPhoto().getDate();
            }
        }
        return null;
    }


    public List<URI> getImagesLinksFromVkRepsponseQuery(GetResponse response) {
        List<URI> images = new ArrayList<>();
        var attaches = response.getItems().get(1).getAttachments();
        for (int i = 0; i < attaches.size(); i++) {
            if(attaches.get(i).getType().equals(WallpostAttachmentType.PHOTO)) {
                images.add(attaches.get(i).getPhoto().getSizes().get(attaches.get(i).getPhoto().getSizes().size() - 1).getUrl());
            }
        }
        log.info("Successfully got image uri's, count {}", images.size());
        return images;
    }
}
