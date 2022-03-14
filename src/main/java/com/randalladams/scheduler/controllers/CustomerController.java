package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.services.CustomerService;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.SceneManager;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * CustomerController class to handle operations with customer tableview
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class CustomerController implements Initializable {

  private static final String customerForm = "/fxml/customerForm.fxml";
  private static ResourceBundle resourceBundle = null;
  private static ResourceBundle langBundle = null;
  private static CustomerService customerService;

  @FXML
  private TableView<Customer> customersTable;

  /**
   * Initializer for customer scene
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    customerService = new CustomerService();
    CustomerController.resourceBundle = resourceBundle;
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    langBundle = ResourceBundle.getBundle("i18n", locale);
    try {
      ObservableList<Customer> allCustomers = customerService.getCustomers();
      setupCustomersTable(allCustomers, resourceBundle);
      // setCustomerTableDimensions();
    } catch (SQLException ex) {
      Database.printSQLException(ex);
    }
  }

  /**
   * method to print all the customer data in a table view
   * @param allCustomers - all the customers in the database
   * @param resourceBundle - resource bundle for lang translations
   */
  private void setupCustomersTable(ObservableList<Customer> allCustomers, ResourceBundle resourceBundle) {
    customersTable.getColumns().clear();

    TableColumn<Customer, String> idCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.id"));
    idCol.setMinWidth(10);
    idCol.setCellValueFactory(
      new PropertyValueFactory<>("id"));

    TableColumn<Customer, String> nameCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.name"));
    nameCol.setMinWidth(100);
    nameCol.setCellValueFactory(
      new PropertyValueFactory<>("name"));

    TableColumn<Customer, String> addressCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.address"));
    addressCol.setMinWidth(150);
    addressCol.setCellValueFactory(
      new PropertyValueFactory<>("address"));

    TableColumn<Customer, String> postalCodeCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.postal_code"));
    postalCodeCol.setMinWidth(50);
    postalCodeCol.setCellValueFactory(
      new PropertyValueFactory<>("postalCode"));

    TableColumn<Customer, String> phoneCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.phone"));
    phoneCol.setMinWidth(50);
    phoneCol.setCellValueFactory(
      new PropertyValueFactory<>("phone"));

    TableColumn<Customer, Date> createDateCol = new TableColumn<>(resourceBundle.getString("customers_table.columns.created"));
    createDateCol.setMinWidth(50);
    createDateCol.setCellValueFactory(
      new PropertyValueFactory<>("createDate"));

    TableColumn<Customer, String> divisionCol = new TableColumn<>("Division");
    divisionCol.setMinWidth(50);
    divisionCol.setCellValueFactory(
      new PropertyValueFactory<>("division")
    );

    TableColumn<Customer, String> countryCol = new TableColumn<>("Country");
    countryCol.setMinWidth(50);
    countryCol.setCellValueFactory(
      new PropertyValueFactory<>("country")
    );

    customersTable.setItems(allCustomers);
    customersTable.setMinSize(800, 400);
    customersTable.getColumns().addAll(
      nameCol,
      addressCol,
      postalCodeCol,
      phoneCol,
      createDateCol,
      divisionCol,
      countryCol);
  }

  /**
   * method to show the edit customer form
   * <p>Lambda Expression</p>
   * <p>Lambda used here to add event listener when the form closes</p>
   * @param actionEvent
   */
  public void editCustomer(ActionEvent actionEvent) {
    try {
      Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
      UserSession.setCurrentCustomerSelected(selectedCustomer.getId());
      Stage stage = new Stage();
      Parent root = FXMLLoader.load(
        CustomerController.class.getResource(customerForm), resourceBundle);
      stage.setScene(new Scene(root, 820, 520));
      stage.setTitle(langBundle.getString("customer_form.edit_title"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(
        ((Node)actionEvent.getSource()).getScene().getWindow() );

      stage.setOnCloseRequest(e -> {
        ObservableList<Customer> allCustomers = null;
        try {
          allCustomers = customerService.getCustomers();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
        setupCustomersTable(allCustomers, resourceBundle);
      });
      stage.show();
    } catch (Exception e) {
      SceneManager.showAlert(Alert.AlertType.ERROR, customersTable.getScene().getWindow(), langBundle.getString("errors.missing_customer_selection.title"),
        langBundle.getString("error.missing_customer.text") + e.getMessage());
    }
  }

  /**
   * Method to show the create customer form
   * <p>Lambda Expression</p>
   * <p>Lambda used here to add event listener when the form closes</p>
   * @param actionEvent
   * @throws IOException
   */
  public void createCustomer(ActionEvent actionEvent) throws IOException {
    UserSession.setCurrentCustomerSelected(0);
    Stage stage = new Stage();
    Parent root = FXMLLoader.load(
      CustomerController.class.getResource(customerForm), resourceBundle);
    stage.setScene(new Scene(root, 820, 520));
    stage.setTitle(langBundle.getString("customer_form.title"));
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(
      ((Node)actionEvent.getSource()).getScene().getWindow());
    // TODO: make this resusable
    stage.setOnCloseRequest(e -> {
      ObservableList<Customer> allCustomers = null;
      try {
        allCustomers = customerService.getCustomers();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
      setupCustomersTable(allCustomers, resourceBundle);
    });
    stage.show();
  }
}
