package com.randalladams.scheduler.customers;

import java.util.Date;

public class Customer {

  private int id;
  private String name;
  private String address;
  private String postalCode;
  private String phone;
  private Date createDate;
  private String createdBy;
  private Date lastUpdateDate;
  private String lastUpdatedBy;
  private int divisionId;

  public Customer(int customer_id, String customer_name, String address, String postal_code, String phone, java.sql.Date create_date, String created_by, java.sql.Date last_update, String last_updated_by, int division_id) {
    this.setId(customer_id);
    this.setName(customer_name);
    this.setAddress(address);
    this.setPostalCode(postal_code);
    this.setPhone(phone);
    this.setCreateDate(create_date);
    this.setCreatedBy(created_by);
    this.setLastUpdateDate(last_update);
    this.setLastUpdatedBy(last_updated_by);
    this.setDivisionId(division_id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public int getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(int divisionId) {
    this.divisionId = divisionId;
  }
}
