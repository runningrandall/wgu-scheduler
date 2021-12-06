package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.model.FirstLevelDivision;
import com.randalladams.scheduler.services.CountryService;
import com.randalladams.scheduler.services.CustomerService;
import com.randalladams.scheduler.services.FirstLevelDivisionsService;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.KeyValuePair;
import javafx.animation.KeyValue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateCustomerController implements Initializable {

  @FXML
  private ChoiceBox<KeyValuePair> firstLevelDivisionChoiceBox;

  @FXML
  private ChoiceBox<KeyValuePair> countriesChoiceBox;

  private static final FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
  private static final CountryService countryService = new CountryService();

  /**
   * Initializer for create customer modal
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      ObservableList<KeyValuePair> countries = countryService.getCountriesKeyValuePairs();
      countriesChoiceBox.setItems(countries);
      // lambda here
      countriesChoiceBox.setOnAction(event -> {
        try {
          filterFirstLevelDivisionsByCountry(countriesChoiceBox.getValue());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      });

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void filterFirstLevelDivisionsByCountry(KeyValuePair selectedCountry) throws SQLException {
    ObservableList<KeyValuePair> firstLevelDivisions = fldService.getFirstLevelDivisionsByCountryId(Integer.parseInt(selectedCountry.getKey()));
    firstLevelDivisionChoiceBox.setItems(firstLevelDivisions);
  }

  public void submitCustomer() {
    System.out.println("submit");
  }
}
