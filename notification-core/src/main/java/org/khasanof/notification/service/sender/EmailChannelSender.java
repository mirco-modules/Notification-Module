package org.khasanof.notification.service.sender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.core.errors.exception.BadRequestAlertException;
import org.khasanof.notification.NotificationDTO;
import org.khasanof.notification.channels.EmailChannelDto;
import org.khasanof.notification.config.MailConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailChannelSender implements ChannelSender<EmailChannelDto> {

    private final MailConfiguration mailConfiguration;

    public EmailChannelSender(MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;
    }

    @Override
    public EmailChannelDto extract(NotificationDTO notificationDto) {
        return notificationDto.getEmailChannelDto();
    }

    @Override
    public void send(EmailChannelDto messageDto) {
        String[] recipients = messageDto.getRecipients().toArray(String[]::new);
        JavaMailSender emailSender = mailConfiguration.getMailSender(messageDto.getSender(), messageDto.getPassword());
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipients);
            helper.setSubject(messageDto.getSubject());
            helper.setText(messageDto.getContent(), true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestAlertException(e.getMessage(), null, "InvalidRequest");
        }
    }
}
