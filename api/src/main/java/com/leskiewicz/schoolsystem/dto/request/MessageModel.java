package com.leskiewicz.schoolsystem.dto.request;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public class MessageModel extends RepresentationModel<MessageModel> {
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
