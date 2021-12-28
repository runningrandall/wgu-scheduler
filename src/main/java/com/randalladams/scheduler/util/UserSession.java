package com.randalladams.scheduler.util;

/**
 * Utility class for managing user sessions
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class UserSession {
  private static UserSession instance;
  private static String userName;
  private static int userId;
  private static int currentCustomerIdSelected;
  private static int currentAppointmentIdSelected;

  /**
   * Constructor for user sessions
   * @param userName string
   * @param userId int
   */
  private UserSession(String userName, int userId) {
    UserSession.userName = userName;
    UserSession.userId = userId;
  }

  /**
   * getter for the user instance
   * @param userName string
   * @param userId int
   */
  public static void getInstance(String userName, int userId) {
    if(instance == null) {
      instance = new UserSession(userName, userId);
    }
  }

  /**
   * getter for username
   * @return string
   */
  public static String getUserName() {
    return userName;
  }

  /**
   * getter for the user id
   * @return int
   */
  public static int getUserId() { return userId; }

  /**
   * setter for the current customer id selected
   * @param customerIdSelected int
   */
  public static void setCurrentCustomerSelected(int customerIdSelected) { currentCustomerIdSelected = customerIdSelected; }

  /**
   * getter for the current customer id
   * @return int
   */
  public static int getCurrentCustomerSelected() { return currentCustomerIdSelected; }

  /**
   * setter for the current appointment id
   * @param appointIdSelected int
   */
  public static void setCurrentAppointmentSelected(int appointIdSelected) { currentAppointmentIdSelected = appointIdSelected; }

  /**
   * getter for the current appointment id
   * @return int
   */
  public static int getCurrentAppointmentSelected() { return currentAppointmentIdSelected; }

  /**
   * method to clean out the user session
   */
  public void cleanUserSession() {
    userName = "";
    userId = 0;
  }
}
