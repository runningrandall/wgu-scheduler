package com.randalladams.scheduler.model;

import java.util.Date;

/**
 * model class for FirstLevelDivision
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class FirstLevelDivision {
  private int divisionId;
  private String division;
  private Date createDate;
  private String createdBy;
  private Date lastUpdateDate;
  private String lastUpdatedBy;
  private int countryId;

  /**
   * Constructor for FirstLevelDivision
   * @param divisionId divisionId
   * @param division division
   * @param createDate createDate
   * @param createdBy createdBy
   * @param lastUpdateDate lastUpdatedDate
   * @param lastUpdatedBy lastUpdatedBy
   * @param countryId countryId
   */
  public FirstLevelDivision(int divisionId, String division, Date createDate, String createdBy, Date lastUpdateDate, String lastUpdatedBy, int countryId) {
    this.divisionId = divisionId;
    this.division = division;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdateDate = lastUpdateDate;
    this.lastUpdatedBy = lastUpdatedBy;
    this.countryId = countryId;
  }
}
