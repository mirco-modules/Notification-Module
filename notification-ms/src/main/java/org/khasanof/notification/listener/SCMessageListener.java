package org.khasanof.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.service.sender.manager.ChannelSenderManager;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author Nurislom
 * @see org.khasanof.notification.listener
 * @since 3/19/2025 3:20 PM
 */
@Slf4j
@RequiredArgsConstructor
@Component(SCMessageListener.NAME)
public class SCMessageListener implements Consumer<NotificationDTO> {

    public static final String NAME = "notification-listener";

    private final ChannelSenderManager channelSenderManager;

    @Override
    public void accept(NotificationDTO message) {
        log.debug("Listener received message : {}", message);
        channelSenderManager.send(message);
    }
}
