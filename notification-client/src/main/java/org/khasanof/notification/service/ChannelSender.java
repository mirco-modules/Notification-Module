package org.khasanof.notification.service;

import org.khasanof.notification.NotificationDto;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service
 * @since 3/15/2025 2:27 PM
 */
public interface ChannelSender {

    void send(NotificationDto notificationDto);
}
