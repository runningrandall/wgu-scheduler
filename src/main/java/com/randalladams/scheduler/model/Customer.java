package com.randalladams.scheduler.model;

import java.time.LocalDateTime;

/**
 * model class for creating customers
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Customer {

  private int id;
  private String name;
  private String address;
  private String postalCode;
  private String phone;
  private LocalDateTime createDate;
  private String createdBy;
  private LocalDateTime lastUpdateDate;
  private String lastUpdatedBy;
  private int divisionId;
  private int countryId;
  private String country;
  private String division;

  /**
   * Constructor for creating a customer
   * @param customer_id customer_id
   * @param customer_name customer_name
   * @param address address
   * @param postal_code postal_code
   * @param phone phone
   * @param create_date create_date
   * @param created_by created_by
   * @param last_update last_update
   * @param last_updated_by last_updated_by
   * @param division_id division_id
   * @param country_id country_id
   * @param country country
   * @param division division
   */
  public Customer(int customer_id, String customer_name, String address, String postal_code, String phone, LocalDateTime create_date, String created_by, LocalDateTime last_update, String last_updated_by, int division_id, int country_id, String country, String division) {
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
    this.setCountryId(country_id);
    this.setCountry(country);
    this.setDivision(division);
  }

  /**
   * setter for division
   * @param division string
   */
  private void setDivision(String division) {
    this.division = division;
  }

  /**
   * getter for division
   * @return string
   */
  public String getDivision() {
    return division;
  }

  /**
   * getter for customer id
   * @return int
   */
  public int getId() {
    return id;
  }

  /**
   * setter for customer id
   * @param id int
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * getter for customer name
   * @return string
   */
  public String getName() {
    return name;
  }

  /**
   * setter for customer name
   * @param name string
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getter for customer address
   * @return string
   */
  public String getAddress() {
    return address;
  }

  /**
   * setter for customer address
   * @param address string
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * getter for postal code
   * @return string
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * setter for postal code
   * @param postalCode string
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * getter for customer phone
   * @return string
   */
  public String getPhone() {
    return phone;
  }

  /**
   * setter for customer phone
   * @param phone string
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * setter for create date
   * @param createDate date
   */
  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  /**
   * setter for createdBy
   * @param createdBy string
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * setter for last update date
   * @param lastUpdateDate date
   */
  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  /**
   * setter for lastUpdatedBy
   * @param lastUpdatedBy string
   */
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /**
   * getter for division id
   * @return int
   */
  public int getDivisionId() {
    return divisionId;
  }

  /**
   * setter for division id
   * @param divisionId int
   */
  public void setDivisionId(int divisionId) {
    this.divisionId = divisionId;
  }

  /**
   * setter for country id
   * @param countryId int
   */
  private void setCountryId(int countryId) {
    this.countryId = countryId;
  }

  /**
   * getter for country id
   * @return int
   */
  public int getCountryId() {
    return countryId;
  }

  /**
   * setter for country
   * @param country string
   */
  private void setCountry(String country) {
    this.country = country;
  }

  /**
   * getter for country
   * @return string
   */
  public String getCountry() {
    return country;
  }

  /**
   * getter for create date
   * @return Date
   */
  public LocalDateTime getCreateDate() {
    return createDate;
  }

  /**
   * getter for created by
   * @return string
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * getter for lastUpdateDate
   * @return Date
   */
  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  /**
   * getter for last updated by
   * @return string
   */
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }
}
