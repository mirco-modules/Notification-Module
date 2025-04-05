package org.khasanof.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.service.config.RestChannelConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service
 * @since 3/15/2025 2:37 PM
 */
@Slf4j
@Service
public class RestChannelSender implements ChannelSender {

    private final RestTemplate restTemplate;

    public RestChannelSender(@Qualifier(RestChannelConfiguration.NAME) RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(NotificationDTO notificationDto) {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/api/messages/send", notificationDto, Void.class);
        if (responseEntity.getStatusCode().isError()) {
            log.warn("Not successfully sent message : {}", notificationDto);
            return;
        }
        log.info("Successfully sent message : {}", notificationDto);
    }
}
