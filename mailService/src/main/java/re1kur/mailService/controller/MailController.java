package re1kur.mailService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.mailService.dto.EmailRequest;
import re1kur.mailService.service.MailService;

@RestController
@RequestMapping("/api/")
public class MailController {
    private final MailService service;

    @Autowired
    public MailController(MailService service) {
        this.service = service;
    }

    @PostMapping("test")
    public ResponseEntity<String> test() {
        service.sendTestMessage();
        return ResponseEntity.ok().body("Successfully sent test message.");
    }


    @PostMapping("send")
    public ResponseEntity<String> send(
            @RequestBody EmailRequest request) {
        service.sendMessage(request);
        return ResponseEntity.ok().body("Successfully sent message.");
    }

}
