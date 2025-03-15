package org.khasanof.notification.service.sender;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.notification.NotificationDto;
import org.khasanof.notification.channels.LogChannelDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogChannelSender implements ChannelSender<LogChannelDto> {

    @Override
    public LogChannelDto extract(NotificationDto notificationDto) {
        return notificationDto.getLogChannelDto();
    }

    @Override
    public void send(LogChannelDto message) {
        log.debug("Message log: {}", message);
    }
}
