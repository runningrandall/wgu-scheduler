package com.randalladams.scheduler.model;

import java.util.Date;

/**
 * model class for creating a country
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Country {
  private int countryId;
  private String country;
  private Date createDate;
  private String createdBy;
  private Date lastUpdate;
  private String lastUpdateBy;

  /**
   * constructor for creating a country
   * @param countryId countryId
   * @param country country
   * @param createDate createDate
   * @param createdBy createdBy
   * @param lastUpdate lastUpdate
   * @param lastUpdateBy lastUpdatedBy
   */
  public Country(int countryId, String country, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy) {
    this.countryId = countryId;
    this.country = country;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdateBy = lastUpdateBy;
  }

  /**
   * getter for country
   * @return string
   */
  public String getCountry() {
    return country;
  }

}
