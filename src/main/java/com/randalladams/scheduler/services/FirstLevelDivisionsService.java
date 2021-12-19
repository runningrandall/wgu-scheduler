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

public class FirstLevelDivisionsService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "first_level_divisions";

  public FirstLevelDivisionsService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

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

  public String getFirstLevelDivisionById(int divisionId) throws SQLException {
    String query = "SELECT Division FROM " + DATABASE_TABLE + " WHERE Division_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    preparedStatement.setInt(1, divisionId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("Division");
  }

  public String getCountryStringFromFirstLevelDivisionId (int divisionId) throws SQLException {
    String query = "SELECT c.Country FROM first_level_divisions fld LEFT join countries c ON c.Country_ID = fld.Country_ID WHERE Division_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    preparedStatement.setInt(1, divisionId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("c.Country");
  }
}
