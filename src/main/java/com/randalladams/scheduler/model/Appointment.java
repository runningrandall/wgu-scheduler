package com.randalladams.scheduler.model;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import com.randalladams.scheduler.util.Database;

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
  private Date start;
  private Date end;
  private Timestamp startTimestamp;
  private Timestamp endTimestamp;
  private Date createDate;
  private String createdBy;
  private Date lastUpdate;
  private String lastUpdatedBy;
  private int customerId;
  private int userId;
  private int contactId;
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
   * @param startTimestamp - appointment start timestamp
   * @param end - appointment end datetime
   * @param endTimestamp - appointment end timestamp
   * @param createDate - appointment created date
   * @param createdBy - appointment who created it
   * @param lastUpdate - appointment last updated datetime
   * @param lastUpdatedBy - appointment who updated the appoinment last
   * @param customerId - appointment customer id
   * @param userId - appointment user id
   * @param contactId - appointment contact id
   */
  public Appointment(int appointmentId, String title, String description, String location, String type, String contactName, Date start, Timestamp startTimestamp, Date end, Timestamp endTimestamp, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
    this.appointmentId = appointmentId;
    this.title = title;
    this.description = description;
    this.location = location;
    this.type = type;
    this.contactName = contactName;
    this.start = start;
    this.startTimestamp = Database.getLocalTimestampFromUtcTimestamp(startTimestamp);
    this.endTimestamp = Database.getLocalTimestampFromUtcTimestamp(endTimestamp);
    this.end = end;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
    this.customerId = customerId;
    this.userId = userId;
    this.contactId = contactId;
    setAppointmentMonthYear(startTimestamp);
    setAppointmentWeekYear(startTimestamp);
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
   * @param startTimestamp timestamp
   */
  public void setAppointmentMonthYear(Timestamp startTimestamp) {
    LocalDate localStartDate = startTimestamp.toLocalDateTime().toLocalDate();
    this.monthYear = localStartDate.format(DateTimeFormatter.ofPattern("MM-yyyy"));
  }

  /**
   * method to set the week year based on start
   * @param startTimestamp timestamp
   */
  public void setAppointmentWeekYear(Timestamp startTimestamp) {
    LocalDate localStartDate = startTimestamp.toLocalDateTime().toLocalDate();
    LocalDate previousOrSameSunday = localStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    this.weekYear = previousOrSameSunday.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
  }

  /**
   * getter for startTimestamp
   * @return timestamp
   */
  public Timestamp getStartTimestamp() {
    return startTimestamp;
  }

  /**
   * getter for endtimestamp
   * @return timestamp
   */
  public Timestamp getEndTimestamp() {
    return endTimestamp;
  }
}
