package org.khasanof.notification.web.rest;

import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.criteria.MessageCriteria;
import org.khasanof.notification.service.dto.MessageDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nurislom
 * @see org.khasanof.notification.web.rest
 * @since 3/15/2025 2:48 PM
 */
@RestController
@RequestMapping("/api/messages")
public class MessageResource extends GeneralQueryResource<Message, MessageDTO, MessageCriteria> {

    public MessageResource(IGeneralService<Message, MessageDTO> generalService, IGeneralQueryService<Message, MessageDTO, MessageCriteria> generalQueryService) {
        super(generalService, generalQueryService);
    }
}
