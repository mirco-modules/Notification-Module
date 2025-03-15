package org.khasanof.notification;

import lombok.*;
import lombok.experimental.Accessors;
import org.khasanof.notification.channels.EmailChannelDto;
import org.khasanof.notification.channels.LogChannelDto;
import org.khasanof.notification.channels.TelegramChannelDto;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Getter
@Setter
public class NotificationDto implements Serializable {

    private LogChannelDto logChannelDto;
    private EmailChannelDto emailChannelDto;
    private TelegramChannelDto telegramChannelDto;
}
