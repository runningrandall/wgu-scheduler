package com.randalladams.scheduler.model;

/**
 * model class for contacts
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Contact {
  private int contactId;
  private String contactName;
  private String email;

  /**
   * constructor for creating a contact
   * @param contactId contactId
   * @param contactName contactName
   * @param email email
   */
  public Contact(int contactId, String contactName, String email) {
    this.contactId = contactId;
    this.contactName = contactName;
    this.email = email;
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

}
