package re1kur.mailService.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import re1kur.mailService.dto.EmailRequest;
import re1kur.mailService.service.MailService;

@Service
public class DefaultMailService implements MailService {
    private final static Logger log = LoggerFactory.getLogger(DefaultMailService.class);
    @Value("${spring.mail.username}")
    private String from;
    private final MailSender mailSender;

    @Autowired
    public DefaultMailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTestMessage() throws MailException {
        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setFrom(from);
        testMessage.setTo("example12.03.2025@gmail.com");
        testMessage.setSubject("DUMMY");
        testMessage.setText("DUMMY BODY");
        mailSender.send(testMessage);
        log.info("Sent test message: {}", testMessage);
    }

    @Override
    public void sendMessage(EmailRequest request) throws MailException  {
        log.info("Sending message: {}", request);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
        log.info("Sent test message: {}", message);
    }
}
