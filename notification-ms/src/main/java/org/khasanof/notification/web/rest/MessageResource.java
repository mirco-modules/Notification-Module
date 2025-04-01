package org.khasanof.notification.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.criteria.MessageCriteria;
import org.khasanof.notification.service.dto.MessageDTO;
import org.khasanof.notification.service.sender.manager.ChannelSenderManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nurislom
 * @see org.khasanof.notification.web.rest
 * @since 3/15/2025 2:48 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
public class MessageResource extends GeneralQueryResource<Message, MessageDTO, MessageCriteria> {

    private final ChannelSenderManager channelSenderManager;

    public MessageResource(IGeneralService<Message, MessageDTO> generalService, IGeneralQueryService<Message, MessageDTO, MessageCriteria> generalQueryService, ChannelSenderManager channelSenderManager) {
        super(generalService, generalQueryService);
        this.channelSenderManager = channelSenderManager;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody NotificationDTO message) {
        log.debug("REST request to send message : {}", message);
        channelSenderManager.send(message);
        return ResponseEntity.ok().build();
    }
}
