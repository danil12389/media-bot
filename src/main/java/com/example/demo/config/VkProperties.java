package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vk")
@AllArgsConstructor
public class VkProperties {
    private int appId;
    private String accessToken;

    public int getAppId() {
        return appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
