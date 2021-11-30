package com.randalladams.scheduler.country;

import com.randalladams.scheduler.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryService {

  private static Connection conn;
  private static final String DATABASE_TABLE = "countries";


  public CountryService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  public ObservableList<Country> getAllCountries() throws SQLException {
    String countriesQuery = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY Country DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(countriesQuery);

    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Country> countryList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      Country country = new Country(
        resultSet.getInt("Country_ID"),
        resultSet.getString("Country"),
        resultSet.getDate("Create_date"),
        resultSet.getString("Created_by"),
        resultSet.getDate("Last_Update"),
        resultSet.getString("Last_Updated_By"));
      countryList.add(country);
    }
    return countryList;
  }

  public static String getCountryById(int countryId) throws SQLException {
    String countryQuery = "SELECT Country FROM " + DATABASE_TABLE + " WHERE Country_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(countryQuery);
    preparedStatement.setInt(1, countryId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("Country");
  }
}
