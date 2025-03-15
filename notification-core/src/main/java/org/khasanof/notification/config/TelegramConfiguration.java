package org.khasanof.notification.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TelegramConfiguration {

    @Value("${telegram.default-sender}")
    private String defaultSender;
}
