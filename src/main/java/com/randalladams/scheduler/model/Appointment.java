package com.randalladams.scheduler.model;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import com.randalladams.scheduler.util.Database;

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

  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContactName() {
    return type;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getContactId() {
    return contactId;
  }

  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  public void setAppointmentMonthYear(Timestamp startTimestamp) {
    LocalDate localStartDate = startTimestamp.toLocalDateTime().toLocalDate();
    this.monthYear = localStartDate.format(DateTimeFormatter.ofPattern("MM-yyyy"));
  }

  public String getAppointmentMonth() {
    return monthYear;
  }

  public void setAppointmentWeekYear(Timestamp startTimestamp) {
    LocalDate localStartDate = startTimestamp.toLocalDateTime().toLocalDate();
    LocalDate previousOrSameSunday = localStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    this.weekYear = previousOrSameSunday.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
  }

  public String getAppointmentWeekYear() {
    return weekYear;
  }

  public Timestamp getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(Timestamp startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public Timestamp getEndTimestamp() {
    return endTimestamp;
  }

  public void setEndTimestamp(Timestamp endTimestamp) {
    this.endTimestamp = endTimestamp;
  }

}
