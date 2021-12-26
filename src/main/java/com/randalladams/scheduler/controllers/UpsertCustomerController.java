package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.services.CountryService;
import com.randalladams.scheduler.services.CustomerService;
import com.randalladams.scheduler.services.FirstLevelDivisionsService;
import com.randalladams.scheduler.util.KeyValuePair;
import com.randalladams.scheduler.util.Lang;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpsertCustomerController implements Initializable {

  @FXML
  private TextField customerId;

  @FXML
  private ChoiceBox<KeyValuePair> firstLevelDivisionChoiceBox;

  @FXML
  private ChoiceBox<KeyValuePair> countriesChoiceBox;

  @FXML
  private TextField customerName;

  @FXML
  private TextField customerAddress;

  @FXML
  private TextField customerPostalcode;

  @FXML
  private TextField customerPhone;

  @FXML
  private Button customerSubmitBtn;
  @FXML
  private Button deleteBtn;

  private static Customer customer;
  private static Boolean isNewCustomer;
  private static final FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
  private static final CountryService countryService = new CountryService();
  private static Alert confirmationAlert;
  private static Alert errorAlert;

  /**
   * Initializer for create customer modal
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      // if 0 we are going to new
      isNewCustomer = UserSession.getCurrentCustomerSelected() == 0;

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

      // populate form if editing
      if (!isNewCustomer) {
        customer = CustomerService.getCustomerById(UserSession.getCurrentCustomerSelected());
        KeyValuePair countryKvP = new KeyValuePair(String.valueOf(customer.getCountryId()), customer.getCountry());
        KeyValuePair fldKvp = new KeyValuePair(String.valueOf(customer.getDivisionId()), customer.getDivision());
        customerId.setText(String.valueOf(customer.getId()));
        customerName.setText(customer.getName());
        customerAddress.setText(customer.getAddress());
        customerPhone.setText(customer.getPhone());
        customerPostalcode.setText(customer.getPostalCode());
        countriesChoiceBox.setValue(countryKvP);
        filterFirstLevelDivisionsByCountry(countryKvP);
        firstLevelDivisionChoiceBox.setValue(fldKvp);
      } else {
        deleteBtn.setVisible(false);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void filterFirstLevelDivisionsByCountry(KeyValuePair selectedCountry) throws SQLException {
    ObservableList<KeyValuePair> firstLevelDivisions = fldService.getFirstLevelDivisionsByCountryId(Integer.parseInt(selectedCountry.getKey()));
    firstLevelDivisionChoiceBox.setItems(firstLevelDivisions);
  }

  // TODO: figure out how to refresh table view after delete
  public void confirmCustomerDelete() throws SQLException {
    confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle(Lang.getString("customer_form.delete.title"));
    confirmationAlert.setContentText(Lang.getString("customer_form.delete.text"));
    confirmationAlert.showAndWait();
    if (confirmationAlert.getResult() == ButtonType.OK) {
      try {
        CustomerService.deleteCustomer(customer.getId());
        Stage stage = (Stage) deleteBtn.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
      } catch (Exception e) {
        System.out.println("Error deleting customer" + e.getMessage());
      }
    }
  }

  public void submitCustomer(ActionEvent event) throws SQLException {
    Boolean isCustomerValid = CustomerService.validateCustomer(
      customerName.getText(),
      customerAddress.getText(),
      customerPostalcode.getText(),
      customerPhone.getText(),
      Integer.parseInt(firstLevelDivisionChoiceBox.getValue().getKey())
    );
    if (isCustomerValid) {
      if (isNewCustomer) {
        CustomerService.createCustomer(
          customerName.getText(),
          customerAddress.getText(),
          customerPostalcode.getText(),
          customerPhone.getText(),
          Integer.parseInt(firstLevelDivisionChoiceBox.getValue().getKey())
        );
      } else {
        CustomerService.editCustomer(
          Integer.parseInt(customerId.getText()),
          customerName.getText(),
          customerAddress.getText(),
          customerPostalcode.getText(),
          customerPhone.getText(),
          Integer.parseInt(firstLevelDivisionChoiceBox.getValue().getKey())
        );
      }
      Stage stage = (Stage) customerSubmitBtn.getScene().getWindow();
      stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    } else {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("customer_form.error.title"));
      errorAlert.setContentText(Lang.getString("customer_form.error.text"));
      errorAlert.show();
    }
  }

}
