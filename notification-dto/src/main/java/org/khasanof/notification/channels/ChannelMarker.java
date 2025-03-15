package org.khasanof.notification.channels;

import java.io.Serializable;
import java.util.List;

public interface ChannelMarker extends Serializable {
    String getSender();

    List<String> getRecipients();

    String getSubject();

    String getContent();
}
