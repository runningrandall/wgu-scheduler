package com.randalladams.scheduler.model;

import com.randalladams.scheduler.util.Database;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.sql.Date;

/**
 * model class for Appointments
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Appointment {
  private int appointmentId;
  private String title;
  private String description;
  private String location;
  private String type;
  private String contactName;
  private ZonedDateTime start;
  private ZonedDateTime end;
  private Date createDate;
  private String createdBy;
  private Date lastUpdate;
  private String lastUpdatedBy;
  private int customerId;
  private int userId;
  private int contactId;
  private String startDisplayString;
  private String endDisplayString;
  private String monthYear;
  private String weekYear;

  /**
   * Constructor for setting up appointments
   * @param appointmentId - appointment id
   * @param title - appointment title
   * @param description - appointment description
   * @param location - appointment location
   * @param type - appointment type
   * @param contactName - appointment contact name
   * @param start - appointment start datetime
   * @param end - appointment end datetime
   * @param createDate - appointment created date
   * @param createdBy - appointment who created it
   * @param lastUpdate - appointment last updated datetime
   * @param lastUpdatedBy - appointment who updated the appoinment last
   * @param customerId - appointment customer id
   * @param userId - appointment user id
   * @param contactId - appointment contact id
   */
  public Appointment(int appointmentId, String title, String description, String location, String type, String contactName, Timestamp start, Timestamp end, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
    this.appointmentId = appointmentId;
    this.title = title;
    this.description = description;
    this.location = location;
    this.type = type;
    this.contactName = contactName;
    this.start = Database.getZonedDateTimeFromTimestamp(start);
    this.end = Database.getZonedDateTimeFromTimestamp(end);
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
    this.customerId = customerId;
    this.userId = userId;
    this.contactId = contactId;
    this.startDisplayString = Database.getDateDisplayString(Database.getZonedDateTimeFromTimestamp(start));
    this.endDisplayString = Database.getDateDisplayString(Database.getZonedDateTimeFromTimestamp(end));
    setAppointmentMonthYear(Database.getZonedDateTimeFromTimestamp(start));
    setAppointmentWeekYear(Database.getZonedDateTimeFromTimestamp(end));
  }

  public ZonedDateTime getStart() {
    return start;
  }

  public ZonedDateTime getEnd() {
    return end;
  }

  public String getStarTime() {
    return start.toString();
  }

  /**
   * getter for appointment id
   * @return int
   */
  public int getAppointmentId() {
    return appointmentId;
  }

  /**
   * getter for title
   * @return int
   */
  public String getTitle() {
    return title;
  }

  /**
   * setter for title
   * @param title - title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * getter for description
   * @return string
   */
  public String getDescription() {
    return description;
  }

  /**
   * getter for location
   * @return string
   */
  public String getLocation() {
    return location;
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
   * @param type type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * getter for contact name
   * @return string
   */
  public String getContactName() {
    return contactName;
  }

  /**
   * setter for contact name
   * @param contactName contactName
   */
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * getter for customer id
   * @return int
   */
  public int getCustomerId() {
    return customerId;
  }

  /**
   * setter for customer id
   * @param customerId int
   */
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  /**
   * getter for user id
   * @return int
   */
  public int getUserId() {
    return userId;
  }

  /**
   * getter for contact id
   * @return int
   */
  public int getContactId() {
    return contactId;
  }

  /**
   * method to set the month year for an appointment
   * @param start ZonedDateTime
   */
  public void setAppointmentMonthYear(ZonedDateTime start) {
    this.monthYear = start.format(DateTimeFormatter.ofPattern("MM-yyyy"));
  }

  /**
   * method to set the week year based on start
   * @param start ZonedDateTime
   */
  public void setAppointmentWeekYear(ZonedDateTime start) {
    LocalDate localStartDate = start.toLocalDate();
    LocalDate previousOrSameSunday = localStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    this.weekYear = previousOrSameSunday.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
  }

  public String getStartDisplayString() {
    return startDisplayString;
  }

  public void setStartDisplayString(String startDisplayString) {
    this.startDisplayString = startDisplayString;
  }

  public String getEndDisplayString() {
    return endDisplayString;
  }

  public void setEndDisplayString(String endDisplayString) {
    this.endDisplayString = endDisplayString;
  }
}
