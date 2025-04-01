package org.khasanof.notification.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.config
 * @since 4/1/2025 9:55 AM
 */
@Configuration
public class RestChannelConfiguration {

    public static final String NAME = "restChannelSenderTemplate";

    @Bean(NAME)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
