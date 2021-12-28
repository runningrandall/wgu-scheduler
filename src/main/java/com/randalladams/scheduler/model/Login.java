package com.randalladams.scheduler.model;

/**
 *
 */
public class Login {
  private String username;
  private String loginDate;
  private String status;

  public Login(String username, String loginDate, String status) {
    this.username = username;
    this.loginDate = loginDate;
    this.status = status;
  }

  public String getUsername() {
    return username;
  }

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
