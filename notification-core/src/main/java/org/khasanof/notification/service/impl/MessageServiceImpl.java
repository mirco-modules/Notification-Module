package org.khasanof.notification.service.impl;

import org.khasanof.core.repository.base.multi.IGeneralMultiTenancyRepository;
import org.khasanof.core.service.base.impl.multi.GeneralMultiTenancyService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.MessageService;
import org.khasanof.notification.service.dto.MessageDTO;
import org.springframework.stereotype.Service;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.impl
 * @since 3/15/2025 2:14 PM
 */
@Service
public class MessageServiceImpl extends GeneralMultiTenancyService<Message, MessageDTO> implements MessageService {

    public MessageServiceImpl(IGeneralMapper<Message, MessageDTO> generalMapper, IGeneralMultiTenancyRepository<Message> generalMultiTenancyRepository) {
        super(generalMapper, generalMultiTenancyRepository);
    }
}
