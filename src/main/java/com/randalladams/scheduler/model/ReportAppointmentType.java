package com.randalladams.scheduler.model;

public class ReportAppointmentType {
  private String type = null;
  private String monthYear = null;
  private int total = 0;

  public ReportAppointmentType(String type, String monthYear, int total) {
    this.type = type;
    this.monthYear = monthYear;
    this.total = total;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMonthYear() {
    return monthYear;
  }

  public void setMonthYear(String monthYear) {
    this.monthYear = monthYear;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
