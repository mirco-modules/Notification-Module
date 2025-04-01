package org.khasanof.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.notification.NotificationDTO;
import org.springframework.stereotype.Service;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service
 * @since 3/15/2025 2:38 PM
 */
@Slf4j
@Service
public class SCStreamChannelSender implements ChannelSender {

    @Override
    public void send(NotificationDTO notificationDto) {

    }
}
