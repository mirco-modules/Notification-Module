package org.khasanof.notification.service.sender;

import org.apache.commons.lang3.StringUtils;
import org.khasanof.notification.NotificationDto;
import org.khasanof.notification.channels.ChannelMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public interface ChannelSender<T extends ChannelMarker> {

    Logger log = LoggerFactory.getLogger(ChannelSender.class);

    default void send(NotificationDto message) {
        T channelDto = extract(message);
        if (Objects.isNull(channelDto)) {
            return;
        }
        Boolean valid = validate(channelDto);
        if (Boolean.FALSE.equals(valid)) {
            return;
        }
        send(channelDto);
    }

    T extract(NotificationDto notificationDto);

    void send(T message);

    default Boolean validate(T channelDto) {
        if (StringUtils.isBlank(channelDto.getSender())) {
            log.debug("Invalid sender");
            return Boolean.FALSE;
        }
        if (Objects.isNull(channelDto.getRecipients()) || channelDto.getRecipients().isEmpty()) {
            log.debug("Invalid recipients");
            return Boolean.FALSE;
        }
        if (StringUtils.isBlank(channelDto.getContent())) {
            log.debug("Invalid content");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
