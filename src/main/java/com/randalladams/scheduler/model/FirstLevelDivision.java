package com.randalladams.scheduler.model;

import java.util.Date;

public class FirstLevelDivision {
  private int divisionId;
  private String division;
  private Date createDate;
  private String createdBy;
  private Date lastUpdateDate;
  private String lastUpdatedBy;
  private int countryId;

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
