package com.randalladams.scheduler.util;

public class Validator {

  private Boolean isValid;
  private String message;

  public Validator(Boolean isValid, String message) {
    this.isValid = isValid;
    this.message = message;
  }

  public Boolean getValid() {
    return isValid;
  }

  public void setValid(Boolean valid) {
    isValid = valid;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
