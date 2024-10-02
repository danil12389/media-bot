package com.example.demo.config;

import com.vk.api.sdk.client.actors.ServiceActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties
@Configuration
public class VkClientConfig {

    private final VkProperties vkProperties;
    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(VkClientConfig.class);

    public VkClientConfig(VkProperties vkProperties) {
        this.vkProperties = vkProperties;
    }

    @Bean("testAppServiceActor")
    public ServiceActor setServiceActor() {
        log.info("Initialize ServiceActor");
        return new ServiceActor(vkProperties.getAppId(), vkProperties.getAccessToken());
    }

}