package com.randalladams.scheduler.model;

/**
 * Model class for the report appointments
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class ReportAppointmentType {
  private String type = null;
  private String monthYear = null;
  private int total = 0;

  /**
   * Constructor for ReportAppointment class
   * @param type string
   * @param monthYear string
   * @param total int
   */
  public ReportAppointmentType(String type, String monthYear, int total) {
    this.type = type;
    this.monthYear = monthYear;
    this.total = total;
  }

  /**
   * getter for type
   * @return string
   */
  public String getType() {
    return type;
  }

  /**
   * setter for type
   * @param type string
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * getter for monthYear
   * @return string
   */
  public String getMonthYear() {
    return monthYear;
  }

  /**
   * getter for total
   * @return int
   */
  public int getTotal() {
    return total;
  }
}
