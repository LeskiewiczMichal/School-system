package com.leskiewicz.schoolsystem.email;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mails")
@AllArgsConstructor
public class MailController {

  private final MailService mailService;

  @PostMapping("/send")
  public void sendSimpleMessage() {
    mailService.sendSimpleMessage("leskiewicz02robocze@gmail.com", "test", "test");
  }
}
