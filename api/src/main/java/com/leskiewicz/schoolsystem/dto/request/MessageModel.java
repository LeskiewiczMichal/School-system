package com.leskiewicz.schoolsystem.dto.request;

import org.springframework.hateoas.EntityModel;

public class MessageModel extends EntityModel<MessageModel> {
  private String message;

  public MessageModel(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
