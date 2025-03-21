package org.khasanof.notification.service.sender.manager;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.service.sender.ChannelSender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.notification.service.sender.manager
 * @since 3/19/2025 3:10 PM
 */
@Slf4j
@Service
@SuppressWarnings({"rawtypes"})
public class ChannelSenderManagerImpl implements ChannelSenderManager, InitializingBean {

    private final Set<ChannelSender> senders = new HashSet<>();

    private final ApplicationContext applicationContext;

    public ChannelSenderManagerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void send(NotificationDTO message) {
        senders.forEach(channelSender -> channelSender.send(message));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        senders.addAll(getAllSenders());
    }

    private Collection<ChannelSender> getAllSenders() {
        return applicationContext.getBeansOfType(ChannelSender.class)
                .values();
    }
}
