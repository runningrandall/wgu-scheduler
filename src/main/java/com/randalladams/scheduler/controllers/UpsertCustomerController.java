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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * UpsertCustomer controller for managing customer form scene
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2021
 */
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
  private static ObservableList<KeyValuePair> countries = null;
  private static ObservableList<KeyValuePair> firstLevelDivisions = null;
  private static final FirstLevelDivisionsService fldService = new FirstLevelDivisionsService();
  private static final CountryService countryService = new CountryService();
  private static Alert confirmationAlert;
  private static Alert errorAlert;

  /**
   * Initializer for create customer scene
   * <p>Lambda Expression</p>
   * <p>Labmda used here to trigger action for selecting a country and filtering first level divisions</p>
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      isNewCustomer = UserSession.getCurrentCustomerSelected() == 0;
      countries = countryService.getCountriesKeyValuePairs();
      countriesChoiceBox.setItems(countries);
      countriesChoiceBox.setOnAction(event -> {
        try {
          filterFirstLevelDivisionsByCountry(countriesChoiceBox.getValue());
        } catch (SQLException e) {
          errorAlert = new Alert(Alert.AlertType.ERROR);
          errorAlert.setTitle(Lang.getString("customer_form.error.title"));
          errorAlert.setContentText(Lang.getString(e.getMessage()));
          errorAlert.show();
        }
      });

      if (!isNewCustomer) {
        populateCustomerForm();
      } else {
        deleteBtn.setVisible(false);
      }
    } catch (SQLException e) {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("customer_form.error.title"));
      errorAlert.setContentText(Lang.getString(e.getMessage()));
      errorAlert.show();
    }
  }

  /**
   * method to populate the customer form when editing
   */
  private void populateCustomerForm() {
    try {
      customer = CustomerService.getCustomerById(UserSession.getCurrentCustomerSelected());
      KeyValuePair countryKvP = new KeyValuePair(String.valueOf(customer.getCountryId()), customer.getCountry());
      KeyValuePair fldKvp = new KeyValuePair(String.valueOf(customer.getDivisionId()), customer.getDivision());
      customerId.setText(String.valueOf(customer.getId()));
      customerName.setText(customer.getName());
      customerAddress.setText(customer.getAddress());
      customerPhone.setText(customer.getPhone());
      customerPostalcode.setText(customer.getPostalCode());
      countriesChoiceBox.setValue(countryKvP);
      countriesChoiceBox.getSelectionModel().select(getSelectedCountryIndex(countryKvP));
      filterFirstLevelDivisionsByCountry(countryKvP);
      firstLevelDivisionChoiceBox.setValue(fldKvp);
      firstLevelDivisionChoiceBox.getSelectionModel().select(getSelectedFldIndex(fldKvp));
    } catch (Exception e) {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("customer_form.error.title"));
      errorAlert.setContentText(Lang.getString(e.getMessage()));
      errorAlert.show();
    }
  }

  /**
   * method to filter fld by country
   * @param selectedCountry
   * @throws SQLException
   */
  public void filterFirstLevelDivisionsByCountry(KeyValuePair selectedCountry) throws SQLException {
    firstLevelDivisions = fldService.getFirstLevelDivisionsByCountryId(Integer.parseInt(selectedCountry.getKey()));
    firstLevelDivisionChoiceBox.setItems(firstLevelDivisions);
  }

  /**
   * method to confirm users want to delete a customer before actually deleting
   */
  public void confirmCustomerDelete() {
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
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(Lang.getString("customer_form.error.title"));
        errorAlert.setContentText("Error deleting customer" + e.getMessage());
        errorAlert.show();
      }
    }
  }

  /**
   * method to get the right country index when loading an existing contact
   * @param countryKvp
   * @return int
   */
  private int getSelectedCountryIndex(KeyValuePair countryKvp) {
    AtomicInteger foundIndex = new AtomicInteger(-1);
    IntStream.range(0, countries.size()).forEach(i -> {
      if (countries.get(i).getKey().equals(countryKvp.getKey())) {
        foundIndex.set(i);
      }
    });
    return foundIndex.get();
  }

  /**
   * method to get the right first level division index based on loaded customer
   * @param fldKvp
   * @return in
   */
  private int getSelectedFldIndex(KeyValuePair fldKvp) {
    AtomicInteger foundIndex = new AtomicInteger(-1);
    IntStream.range(0, firstLevelDivisions.size()).forEach(i -> {
      if (firstLevelDivisions.get(i).getKey().equals(fldKvp.getKey())) {
        foundIndex.set(i);
      }
    });
    return foundIndex.get();
  }

  /**
   * method to submit the customer to apply updates/creates
   * the method first validatest the customer before saving and shows an alert if something goes wrong
   * @param event
   * @throws SQLException
   */
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
