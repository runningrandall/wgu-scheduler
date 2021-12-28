package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Contact;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.KeyValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class for managing contacts
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class ContactService {

  private static Connection conn;
  private static final String DATABASE_TABLE = "contacts";

  /**
   * Constructor for ContactService
   * Always creates a database connection
   */
  public ContactService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  /**
   * method to get all contacts
   * @deprecated
   * @return ObservableList of Contacts
   * @throws SQLException
   */
  public ObservableList<Contact> getAllContacts() throws SQLException {
    String contactsQuery = "SELECT * FROM " + DATABASE_TABLE;
    PreparedStatement preparedStatement = conn.prepareStatement(contactsQuery);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      Contact contact = new Contact(
        resultSet.getInt("Contact_ID"),
        resultSet.getString("Contact_Name"),
        resultSet.getString("Email")
      );
      contactList.add(contact);
    }
    return contactList;
  }

  /**
   * gets a list of contact key value pairs
   * @return list of key value pairs
   * @throws SQLException
   */
  public ObservableList<KeyValuePair> getContactKeyValuePairs() throws SQLException {
    String query = "SELECT Contact_ID, Contact_Name FROM " + DATABASE_TABLE + " ORDER BY Contact_Name ASC";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<KeyValuePair> contacts = FXCollections.observableArrayList();
    while (resultSet.next()) {
      String contactId = resultSet.getString("Contact_ID");
      String contactName = resultSet.getString("Contact_Name");
      KeyValuePair fldKeyValuePair = new KeyValuePair(contactId, contactName);
      contacts.add(fldKeyValuePair);
    }
    return contacts;
  }

}
