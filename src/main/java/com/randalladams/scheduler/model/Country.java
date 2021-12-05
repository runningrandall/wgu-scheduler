package com.randalladams.scheduler.model;

import java.util.Date;

public class Country {
  private int countryId;
  private String country;
  private Date createDate;
  private String createdBy;
  private Date lastUpdate;
  private String lastUpdateBy;


  public Country(int countryId, String country, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy) {
    this.countryId = countryId;
    this.country = country;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdateBy = lastUpdateBy;
  }

  public int getCountryId() {
    return countryId;
  }

  public String getCountry() {
    return country;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public String getLastUpdateBy() {
    return lastUpdateBy;
  }
}
