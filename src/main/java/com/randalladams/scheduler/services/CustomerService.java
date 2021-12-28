package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Service class for managing customers
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class CustomerService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "customers";
  private static final FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
  private static final CountryService countryService = new CountryService();
  private static final String uk = "UK";

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

  /**
   * Method to get all customers
   * @return ObservableList of Customers
   * @throws SQLException
   */
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
          resultSet.getInt("fld.Country_ID"),
          countryService.getCountryById(resultSet.getInt("fld.Country_ID")),
          fldService.getFirstLevelDivisionById(resultSet.getInt("c.Division_ID")));
          customerList.add(customer);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      } catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return customerList;
  }

  /**
   * Gets a customer based on customer id
   * @param customerId int
   * @return Customer
   * @throws SQLException
   */
  public static Customer getCustomerById(int customerId) throws SQLException {
    String sql = "SELECT * FROM " + DATABASE_TABLE + " c " +
      "LEFT JOIN first_level_divisions fld " +
      "ON fld.Division_ID = c.Division_ID " +
      "WHERE c.Customer_ID = ? " +
      "ORDER BY c.Create_date DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    preparedStatement.setInt(1, customerId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return new Customer(
      customerId,
      resultSet.getString("c.Customer_Name"),
      resultSet.getString("c.Address"),
      resultSet.getString("c.Postal_Code"),
      resultSet.getString("c.Phone"),
      resultSet.getDate("c.Create_Date"),
      resultSet.getString("c.Created_By"),
      resultSet.getDate("c.Last_Update"),
      resultSet.getString("c.Last_Updated_By"),
      resultSet.getInt("c.Division_ID"),
      resultSet.getInt("fld.Country_ID"),
      countryService.getCountryById(resultSet.getInt("fld.Country_ID")),
      fldService.getFirstLevelDivisionById(resultSet.getInt("c.Division_ID"))
    );
  }

  /**
   * Method to update a customer based on id
   * @param customerId int
   * @param name string
   * @param address string
   * @param postalCode string
   * @param phone string
   * @param divisionId int
   * @return Customer
   * @throws SQLException
   */
  public static Customer editCustomer(int customerId, String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
    Customer editCustomer;
    String updateQuery = "UPDATE " + DATABASE_TABLE + " " +
      "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
      "WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
    Date currentDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

    preparedStatement.setString(1, name);
    preparedStatement.setString(2, address);
    preparedStatement.setString(3, postalCode);
    preparedStatement.setString(4, phone);
    preparedStatement.setDate(5, sqlDate);
    preparedStatement.setString(6, UserSession.getUserName());
    preparedStatement.setInt(7, divisionId);
    preparedStatement.setInt(8, customerId);

    preparedStatement.executeUpdate();

    return getCustomerById(customerId);
  }

  /**
   * Method to insert a customer in to the database
   * @param name string
   * @param address string
   * @param postalCode string
   * @param phone string
   * @param divisionId int
   * @return Customer
   * @throws SQLException
   */
  public static Customer createCustomer(String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
    Customer newCustomer;
    String insertQuery = "INSERT INTO " + DATABASE_TABLE +
      "(`Customer_Name`, `Address`, `Postal_Code`, `Phone`, `Create_Date`, `Created_By`, `Last_Update`, `Last_Updated_By`, `Division_ID`) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
    Date currentDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

    preparedStatement.setString(1, name);
    preparedStatement.setString(2, address);
    preparedStatement.setString(3, postalCode);
    preparedStatement.setString(4, phone);
    preparedStatement.setDate(5, sqlDate);
    preparedStatement.setString(6, UserSession.getUserName());
    preparedStatement.setDate(7, sqlDate);
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

  /**
   * method to validate a customer before creating/updating
   * @param name string
   * @param address string
   * @param postalCode string
   * @param phoneNumber string
   * @param firstLevelDivisionId int
   * @return bool
   * @throws SQLException
   */
  public static boolean validateCustomer(String name, String address, String postalCode, String phoneNumber, int firstLevelDivisionId) throws SQLException {
    boolean isAnyEmpty = name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phoneNumber.isEmpty() || firstLevelDivisionId == 0;
    if (isAnyEmpty) {
      return false;
    }

    boolean isValidAddress;
    FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
    String usAndCaAddressPattern = "(\\d)* ([a-zA-Z ])*, ([a-zA-Z ])*";
    String ukAddressPattern = "(\\d)* ([a-zA-Z ])*, ([a-zA-Z ])*, ([a-zA-Z ])*";
    String phonePattern = "((\\d)*-)*(\\d)*";
    String country = fldService.getCountryStringFromFirstLevelDivisionId(firstLevelDivisionId);

    // TODO: not hard code this...db can change and break this
    switch(country) {
      case uk:
        isValidAddress = Pattern.matches(ukAddressPattern, address);
        break;
      default:
        isValidAddress = Pattern.matches(usAndCaAddressPattern, address);
        break;
    }

    boolean isValidPhone = Pattern.matches(phonePattern, phoneNumber);

    return isValidAddress && isValidPhone;
  }

  /**
   * method to delete a customer by id
   * must first delete all appointments
   * @param customerId int
   * @throws SQLException
   */
  public static void deleteCustomer(int customerId) throws SQLException {
    // first delete all appointments
    AppointmentService appointmentService = new AppointmentService();
    appointmentService.deleteAllAppointmentsByCustomerId(customerId);
    // now delete the customer
    String deleteQuery = "DELETE FROM " + DATABASE_TABLE + " WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
    preparedStatement.setInt(1, customerId);
    preparedStatement.executeUpdate();
  }
}
