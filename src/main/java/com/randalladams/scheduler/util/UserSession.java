package com.randalladams.scheduler.util;

public class UserSession {
  private static UserSession instance;
  private static String userName;
  private static int userId;
  private static int currentCustomerIdSelected;
  private static int currentAppointmentIdSelected;

  private UserSession(String userName, int userId) {
    UserSession.userName = userName;
    UserSession.userId = userId;
  }

  public static void getInstance(String userName, int userId) {
    if(instance == null) {
      instance = new UserSession(userName, userId);
    }
  }

  public static String getUserName() {
    return userName;
  }
  public static int getUserId() { return userId; }

  public static void setCurrentCustomerSelected(int customerIdSelected) { currentCustomerIdSelected = customerIdSelected; }
  public static int getCurrentCustomerSelected() { return currentCustomerIdSelected; }

  public static void setCurrentAppointmentSelected(int appointIdSelected) { currentAppointmentIdSelected = appointIdSelected; }
  public static int getCurrentAppointmentSelected() { return currentAppointmentIdSelected; }

  public void cleanUserSession() {
    userName = "";
    userId = 0;
  }
}
