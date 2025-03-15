package org.khasanof.notification.service;

import org.khasanof.core.service.base.IGeneralMultiTenancyService;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.dto.MessageDTO;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service
 * @since 3/15/2025 2:10 PM
 */
public interface MessageService extends IGeneralMultiTenancyService<Message, MessageDTO> {
}
