package com.randalladams.scheduler.util;

public class UserSession {
  private static UserSession instance;
  private static String userName;
  private static int currentCustomerIdSelected;

  private UserSession(String userName) {
    UserSession.userName = userName;
  }

  public static void getInstance(String userName) {
    if(instance == null) {
      instance = new UserSession(userName);
    }
  }

  public static String getUserName() {
    return userName;
  }

  public static void setCurrentCustomerSelected(int customerIdSelected) { currentCustomerIdSelected = customerIdSelected; }
  public static int getCurrentCustomerSelected() { return currentCustomerIdSelected; }

  public void cleanUserSession() {
    userName = "";
  }
}
