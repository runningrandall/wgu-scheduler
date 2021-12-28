package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Country;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.KeyValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class for countries
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class CountryService {

  private static Connection conn;
  private static final String DATABASE_TABLE = "countries";

  /**
   * Constructor for CountryService
   * Always connects to the database
   */
  public CountryService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  /**
   * method to get all the countries
   * @return ObservableList of Countries
   * @throws SQLException
   */
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

  /**
   * method to get a country by id
   * @param countryId int
   * @return Country
   * @throws SQLException
   */
  public static String getCountryById(int countryId) throws SQLException {
    String countryQuery = "SELECT Country FROM " + DATABASE_TABLE + " WHERE Country_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(countryQuery);
    preparedStatement.setInt(1, countryId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getString("Country");
  }

  /**
   * method to get all the countries in a key value pair format
   * @return ObservableList of Countries in Key Value Pair format
   * @throws SQLException
   */
  public ObservableList<KeyValuePair> getCountriesKeyValuePairs() throws SQLException {
    String query = "SELECT Country_ID, Country FROM " + DATABASE_TABLE + " ORDER BY Country ASC";
    PreparedStatement preparedStatement = conn.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<KeyValuePair> countries = FXCollections.observableArrayList();
    while (resultSet.next()) {
      String countryId = resultSet.getString("Country_ID");
      String country = resultSet.getString("Country");
      KeyValuePair fldKeyValuePair = new KeyValuePair(countryId, country);
      countries.add(fldKeyValuePair);
    }
    return countries;
  }
}
