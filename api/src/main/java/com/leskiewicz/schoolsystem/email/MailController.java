package com.leskiewicz.schoolsystem.email;

import com.leskiewicz.schoolsystem.email.dto.SendSimpleMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mails")
@AllArgsConstructor
public class MailController {

  private final MailService mailService;

  @PostMapping("/contact")
  public ResponseEntity<String> sendSimpleMessage(@RequestBody SendSimpleMessageRequest request) {
    mailService.sendSimpleMessage(
        "leskiewicz02robocze@gmail.com", request.getSubject(), request.getMessage());
    return ResponseEntity.ok("Email sent successfully");
  }
}
