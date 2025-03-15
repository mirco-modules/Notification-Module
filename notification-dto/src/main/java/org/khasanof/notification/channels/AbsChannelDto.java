package org.khasanof.notification.channels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AbsChannelDto implements ChannelMarker, Serializable {

    private String sender;
    private List<String> recipients;
    private String subject;
    private String content;
}
