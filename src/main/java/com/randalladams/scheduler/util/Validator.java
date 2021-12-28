package com.randalladams.scheduler.util;

/**
 * Utility validator class for displaying of errors and valid states
 */
public class Validator {

  private Boolean isValid;
  private String message;

  /**
   * Constructor
   * @param isValid bool
   * @param message string
   */
  public Validator(Boolean isValid, String message) {
    this.isValid = isValid;
    this.message = message;
  }

  /**
   * getter for isValid
   * @return bool
   */
  public Boolean getValid() {
    return isValid;
  }

  /**
   * getter for message
   * @return string
   */
  public String getMessage() {
    return message;
  }
}
