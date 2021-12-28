package com.randalladams.scheduler.model;

/**
 * model class for logins
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Login {
  private String username;
  private String loginDate;
  private String status;

  /**
   * Constructor for the login class
   * @param username string
   * @param loginDate string
   * @param status string
   */
  public Login(String username, String loginDate, String status) {
    this.username = username;
    this.loginDate = loginDate;
    this.status = status;
  }

  /**
   * getter for username
   * @return string
   */
  public String getUsername() {
    return username;
  }

  /**
   * setter for username
   * @param username string
   */
  public void setUsername(String username) {
    this.username = username;
  }

  public String getLoginDate() {
    return loginDate;
  }

  public void setLoginDate(String loginDate) {
    this.loginDate = loginDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
