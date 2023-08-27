package com.leskiewicz.schoolsystem.email.dto;

public class SendSimpleMessageRequest {

  private String subject;
  private String message;

  public SendSimpleMessageRequest() {}

  public SendSimpleMessageRequest(String subject, String message) {
    this.subject = subject;
    this.message = message;
  }

  public String getSubject() {
    return subject;
  }

  public String getMessage() {
    return message;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
