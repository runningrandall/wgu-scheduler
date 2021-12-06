package com.randalladams.scheduler.services;
/*
 * CustomersService is the service for managing CRUD operations on the Customers table
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

public class CustomerService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "customers";
  private static final FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();

  /**
   * constructor to create connection to the database
   */
  public CustomerService() {
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
          CountryService.getCountryById(resultSet.getInt("fld.Country_ID")),
          fldService.getFirstLevelDivisionById(resultSet.getInt("c.Division_ID")));
          customerList.add(customer);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return customerList;
  }

  public static Customer getCustomerById(int customerId) throws SQLException {
    String sql = "SELECT * FROM " + DATABASE_TABLE + " WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    preparedStatement.setInt(1, customerId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return new Customer(
      customerId,
      resultSet.getString("Customer_Name"),
      resultSet.getString("Address"),
      resultSet.getString("Postal_Code"),
      resultSet.getString("Phone"),
      resultSet.getDate("Create_Date"),
      resultSet.getString("Created_By"),
      resultSet.getDate("Last_Update"),
      resultSet.getString("Last_Updated_By"),
      resultSet.getInt("Division_ID"),
      CountryService.getCountryById(resultSet.getInt("fld.Country_ID")),
      fldService.getFirstLevelDivisionById(resultSet.getInt("c.Division_ID"))
    );
  }
  public static Customer createCustomer(String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
    Customer newCustomer;
    String insertQuery = "INSERT INTO " + DATABASE_TABLE +
      "(`Customer_Name`, `Address`, `Postal_Code`, `Phone`, `Create_Date`, `Created_By`, `Last_Update`, `Last_Updated_By`, `Division_ID`) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
    Date currentDate = new Date();

    preparedStatement.setString(1, name);
    preparedStatement.setString(2, address);
    preparedStatement.setString(3, postalCode);
    preparedStatement.setString(4, phone);
    preparedStatement.setDate(5, (java.sql.Date) currentDate);
    preparedStatement.setString(6, UserSession.getUserName());
    preparedStatement.setDate(7, (java.sql.Date) currentDate);
    preparedStatement.setString(8, UserSession.getUserName());
    preparedStatement.setInt(9, divisionId);

    int affectedRows = preparedStatement.executeUpdate();

    if (affectedRows == 0) {
      throw new SQLException("Creating customer failed");
    }

    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        int newCustomerId = generatedKeys.getInt(1);
        newCustomer = getCustomerById(newCustomerId);
      }
      else {
        throw new SQLException("Creating customer failed, missing id");
      }
    }
    return newCustomer;
  }
}
