package org.khasanof.notification.service.sender.manager;

import org.khasanof.notification.NotificationDTO;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.sender.manager
 * @since 3/19/2025 3:09 PM
 */
public interface ChannelSenderManager {

    void send(NotificationDTO message);
}
