package re1kur.mailService.service;

import re1kur.mailService.dto.EmailRequest;

public interface MailService {
    void sendTestMessage();

    void sendMessage(EmailRequest request);

}
