package org.khasanof.notification.channels;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailChannelDto extends AbsChannelDto implements Serializable {

    private String password;
}
