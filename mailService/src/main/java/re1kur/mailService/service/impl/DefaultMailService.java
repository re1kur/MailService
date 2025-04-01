package re1kur.mailService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import re1kur.mailService.client.DatabaseClient;
import re1kur.mailService.dto.EmailRequest;
import re1kur.mailService.dto.ObjectChangesEvent;
import re1kur.mailService.service.MailService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class DefaultMailService implements MailService {
    private final static Logger log = LoggerFactory.getLogger(DefaultMailService.class);

    @Value("${spring.mail.username}")
    private String from;

    private final MailSender mailSender;

    private final DatabaseClient client;

    @Autowired
    public DefaultMailService(MailSender mailSender,
                              DatabaseClient client) {
        this.mailSender = mailSender;
        this.client = client;
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
    public void sendMessage(EmailRequest request) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
    }

    @RabbitListener(queues = "${custom.queue.verification}")
    public void listenVerification(Message message) throws MailException {
        try {
            String content = new String(message.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> map = mapper.readValue(content, Map.class);

            EmailRequest emailRequest = EmailRequest.builder()
                    .body(map.get("body"))
                    .subject(map.get("subject"))
                    .to(map.get("to"))
                    .build();
            log.info("Sending message: {}", emailRequest);
            sendMessage(emailRequest);
            log.info("Sent verification message: {}", emailRequest);
        } catch (MailException e) {
            log.error("Error while sending verification email", e);
        } catch (JsonProcessingException e) {
        }
    }

    @RabbitListener(queues = "${custom.queue.notification}")
    public void listenNotification(Message message) throws MailException {
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            ObjectChangesEvent objectChangesEvent = mapper.readValue(content, ObjectChangesEvent.class);
            List<String> emails = client.getSubscribersEmails(objectChangesEvent.getObjectChangesName(), objectChangesEvent.getObjectChangesId());
            log.info(emails.toString());
            if (emails.isEmpty()) return;
            for (String email : emails) {
                log.info("Subscriber {} should receive email request.", email);
                EmailRequest request = EmailRequest.builder()
                        .to(email)
                        .subject(objectChangesEvent.getObjectChangesName() + " CHANGES NOTIFICATION")
                        .body(objectChangesEvent.getChangesBodyMessage()
                              + "\nChanges at: " + objectChangesEvent.getChangesTimestamp())
                        .build();
                sendMessage(request);
                log.info("Sending message to {}: {}", email, request);
            }
        } catch (JsonProcessingException e) {
            log.error("Error while serializing received json into ObjectChangesEvent", e);
        }
    }

    @RabbitListener(queues = "test")
    public void listenTest() throws MailException {
        sendTestMessage();
    }

}
