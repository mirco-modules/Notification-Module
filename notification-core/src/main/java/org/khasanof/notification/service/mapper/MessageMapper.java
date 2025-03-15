package org.khasanof.notification.service.mapper;

import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.dto.MessageDTO;
import org.mapstruct.Mapper;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.mapper
 * @since 3/15/2025 2:13 PM
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends IGeneralMapper<Message, MessageDTO> {
}
