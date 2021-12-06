package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.model.FirstLevelDivision;
import com.randalladams.scheduler.services.CustomerService;
import com.randalladams.scheduler.services.FirstLevelDivisionsService;
import com.randalladams.scheduler.util.Database;
import javafx.animation.KeyValue;
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
  private ChoiceBox<FirstLevelDivisionsService.KeyValuePair> firstLevelDivisionChoiceBox;

  /**
   * Initializer for create customer modal
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
    try {
      ObservableList<FirstLevelDivisionsService.KeyValuePair> firstLevelDivisions = fldService.getFirstLevelDivisionsKeyValuePairs();
      firstLevelDivisionChoiceBox.setItems(firstLevelDivisions);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void submitCustomer() {
    System.out.println("submit");
  }
}
