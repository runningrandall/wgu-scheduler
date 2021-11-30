package com.randalladams.scheduler.customers;
/*
 * CustomersService is the service for managing CRUD operations on the Customers table
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.country.CountryService;
import com.randalladams.scheduler.login.LoginService;
import com.randalladams.scheduler.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    String customersQuery = "SELECT * FROM " + DATABASE_TABLE + " c " +
      "LEFT JOIN first_level_divisions fld " +
      "ON fld.Division_ID = c.Division_ID " +
      "ORDER BY c.Create_date DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(customersQuery);

    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    CountryService countryService = new CountryService();
    while (resultSet.next()) {
      try {
        Customer customer = new Customer(
          resultSet.getInt("c.Customer_ID"),
          resultSet.getString("c.Customer_Name"),
          resultSet.getString("c.Address"),
          resultSet.getString("c.Postal_Code"),
          resultSet.getString("c.Phone"),
          resultSet.getDate("c.Create_Date"),
          resultSet.getString("c.Created_By"),
          resultSet.getDate("c.Last_Update"),
          resultSet.getString("c.Last_Updated_By"),
          resultSet.getInt("c.Division_ID"),
          countryService.getCountryById(resultSet.getInt("fld.Country_ID")));
          customerList.add(customer);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return customerList;
  }
}
