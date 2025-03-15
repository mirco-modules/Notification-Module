package org.khasanof.notification.service.query;

import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.query.specification.DynamicSpecificationQueryService;
import org.khasanof.core.service.query.specification.core.helper.CriteriaFieldResolver;
import org.khasanof.core.service.query.specification.core.manager.DynamicSpecificationBuilderManager;
import org.khasanof.notification.domain.Message;
import org.khasanof.notification.service.criteria.MessageCriteria;
import org.khasanof.notification.service.dto.MessageDTO;
import org.springframework.stereotype.Service;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.query
 * @since 3/15/2025 2:14 PM
 */
@Service
public class MessageQueryService extends DynamicSpecificationQueryService<Message, MessageDTO, MessageCriteria> {

    public MessageQueryService(IGeneralMapper<Message, MessageDTO> generalMapper,
                               IGeneralRepository<Message> generalRepository,
                               CriteriaFieldResolver criteriaFieldResolver,
                               DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
