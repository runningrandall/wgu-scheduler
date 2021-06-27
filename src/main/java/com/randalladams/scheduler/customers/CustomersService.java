package com.randalladams.scheduler.customers;
/*
 * CustomersService is the service for managing CRUD operations on the Customers table
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomersService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "customers";

  /**
   * constructor to create connection to the database
   */
  public CustomersService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  public ObservableList<Customer> getCustomers() throws SQLException {
    String customersQuery = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY Create_date DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(customersQuery);

    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      Customer customer = new Customer(
        resultSet.getInt("Customer_ID"),
        resultSet.getString("Customer_Name"),
        resultSet.getString("Address"),
        resultSet.getString("Postal_Code"),
        resultSet.getString("Phone"),
        resultSet.getDate("Create_Date"),
        resultSet.getString("Created_By"),
        resultSet.getDate("Last_Update"),
        resultSet.getString("Last_Updated_By"),
        resultSet.getInt("Division_ID"));
      customerList.add(customer);
    }
    return customerList;
  }
}
