package com.randalladams.scheduler.util;

public class UserSession {
  private static UserSession instance;
  private static String userName;

  private UserSession(String userName) {
    this.userName = userName;
  }

  public static UserSession getInstance(String userName) {
    if(instance == null) {
      instance = new UserSession(userName);
    }
    return instance;
  }

  public static String getUserName() {
    return userName;
  }

  public void cleanUserSession() {
    userName = "";
  }
}
