package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.FirstLevelDivision;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.KeyValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class for managing first level divisions
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class FirstLevelDivisionsService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "first_level_divisions";

  /**
   * Constructor that always connects to the database
   */
  public FirstLevelDivisionsService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  /**
   * Method to get all of the first level divisions
   * @return ObservableList of FirstLevelDivisions
   * @throws SQLException
   */
  public ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
    String query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY Division DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<FirstLevelDivision> firstLevelDivisionsList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      FirstLevelDivision fld = new FirstLevelDivision(
        resultSet.getInt("Division_ID"),
        resultSet.getString("Division"),
        resultSet.getDate("Create_Date"),
        resultSet.getString("Created_By"),
        resultSet.getDate("Last_Update"),
        resultSet.getString("Last_Updated_By"),
        resultSet.getInt("Country_ID"));
      firstLevelDivisionsList.add(fld);
    }
    return firstLevelDivisionsList;
  }

  /**
   * Method to get the first level divisions in key value pair format
   * @return ObservableList of FirstLevelDivisions in key value pair format
   * @throws SQLException
   */
  public ObservableList<KeyValuePair> getFirstLevelDivisionsKeyValuePairs() throws SQLException {
    String query = "SELECT Division_ID, Division FROM " + DATABASE_TABLE + " ORDER BY Division DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<KeyValuePair> firstLevelDivisionsList = FXCollections.observableArrayList();
    while (resultSet.next()) {
      String divisionId = resultSet.getString("Division_ID");
      String division = resultSet.getString("Division");
      KeyValuePair fldKeyValuePair = new KeyValuePair(divisionId, division);
      firstLevelDivisionsList.add(fldKeyValuePair);
    }
    return firstLevelDivisionsList;
  }

  /**
   * method to get the first level divisions in key value pair format but filtered by country id
   * @param countryId int
   * @return ObservableList of FirstLevelDivisions in key value pair format
   * @throws SQLException
   */
  public ObservableList<KeyValuePair> getFirstLevelDivisionsByCountryId(int countryId) throws SQLException {
    String query = "SELECT Division_ID, Division FROM " + DATABASE_TABLE + " WHERE Country_ID = ? ORDER BY Division ASC";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    preparedStatement.setInt(1, countryId);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<KeyValuePair> firstLevelDivisionsList = FXCollections.observableArrayList();
    while (resultSet.next()) {
      String divisionId = resultSet.getString("Division_ID");
      String division = resultSet.getString("Division");
      KeyValuePair fldKeyValuePair = new KeyValuePair(divisionId, division);
      firstLevelDivisionsList.add(fldKeyValuePair);
    }
    return firstLevelDivisionsList;
  }

  /**
   * method to get a first level division by id
   * @param divisionId int
   * @return first level division
   * @throws SQLException
   */
  public String getFirstLevelDivisionById(int divisionId) throws SQLException {
    String query = "SELECT Division FROM " + DATABASE_TABLE + " WHERE Division_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    preparedStatement.setInt(1, divisionId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("Division");
  }

  /**
   * method to get the country string from a division id
   * @param divisionId int
   * @return string
   * @throws SQLException
   */
  public String getCountryStringFromFirstLevelDivisionId (int divisionId) throws SQLException {
    String query = "SELECT c.Country FROM first_level_divisions fld LEFT join countries c ON c.Country_ID = fld.Country_ID WHERE Division_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    preparedStatement.setInt(1, divisionId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("c.Country");
  }
}
