package re1kur.mailService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import re1kur.mailService.service.MailService;

import java.util.Objects;

@Slf4j
@Service
public class DefaultMailService implements MailService {

    @Value("${spring.mail.username}")
    private String from;
    private MailSender mailSender;

    @Autowired
    public DefaultMailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendTestMessage(String to, String subject, String body) throws MailException {
        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setFrom(from);
        testMessage.setTo(Objects.requireNonNullElse(to, "example12.03.2025@gmail.com"));
        testMessage.setSubject(Objects.requireNonNullElse(subject, "DUMMY"));
        testMessage.setText(Objects.requireNonNullElse(body, "DUMMY BODY"));
        mailSender.send(testMessage);
        log.info("Sent test message: {}", testMessage);
    }
}
