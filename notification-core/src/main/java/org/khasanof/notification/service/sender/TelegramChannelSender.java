package org.khasanof.notification.service.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.channels.TelegramChannelDto;
import org.khasanof.notification.config.TelegramConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramChannelSender implements ChannelSender<TelegramChannelDto> {

    private final RestTemplate restTemplate;
    private final TelegramConfiguration telegramConfiguration;

    @Override
    public TelegramChannelDto extract(NotificationDTO notificationDto) {
        return notificationDto.getTelegramChannelDto();
    }

    @Override
    public void send(TelegramChannelDto message) {
        log.debug("Request to send message for Telegram Channel: {}", message);
        if (StringUtils.isBlank(message.getSender())) {
            message.setSender(telegramConfiguration.getDefaultSender());
        }
        sendMessage(message);
    }

    private void sendMessage(TelegramChannelDto message) {
        message.getRecipients()
                .forEach(recipient -> {
                    ResponseEntity<String> response = restTemplate.exchange(
                            "https://api.telegram.org/bot" +
                                    message.getSender() +
                                    "/sendMessage?chat_id=" +
                                    recipient +
                                    "&text=" +
                                    message.getContent(),
                            HttpMethod.POST,
                            null,
                            String.class
                    );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        log.debug("From: {} to: {} this message successfully sent: {}", message.getSender(), recipient, message.getContent());
                        return;
                    }
                    log.error("From: {} to: {} this message failed sent: {}", message.getSender(), recipient, message.getContent());
                });
    }
}
