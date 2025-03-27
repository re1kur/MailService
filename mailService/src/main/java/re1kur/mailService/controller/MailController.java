package re1kur.mailService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.mailService.service.MailService;

@RestController
@RequestMapping("/api/")
public class MailController {
    private MailService service;

    @Autowired
    public MailController(MailService service) {
        this.service = service;
    }

    @GetMapping("test")
    public ResponseEntity<String> test(
            @RequestBody(required = false) String to,
            @RequestBody(required = false) String subject,
            @RequestBody(required = false) String body) {
        service.sendTestMessage(to, subject, body);
        return ResponseEntity.ok().body("Successfully sent.");
    }

}
