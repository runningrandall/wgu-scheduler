package com.randalladams.scheduler.model;

import java.util.Date;

public class Appointment {
  private int appointmentId;
  private String title;
  private String description;
  private String location;
  private String type;
  private Date start;
  private Date end;
  private Date createDate;
  private String createdBy;
  private Date lastUpdate;
  private String lastUpdatedBy;
  private int customerId;
  private int userId;
  private int contactId;

  public Appointment(int appointmentId, String title, String description, String location, String type, Date start, Date end, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
    this.appointmentId = appointmentId;
    this.title = title;
    this.description = description;
    this.location = location;
    this.type = type;
    this.start = start;
    this.end = end;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
    this.customerId = customerId;
    this.userId = userId;
    this.contactId = contactId;
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
}
