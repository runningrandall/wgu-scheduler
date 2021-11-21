package com.randalladams.scheduler.customers;
/*
 * CustomersController is the controller for CRUD operations on Customers
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.SceneManager;
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
import javafx.stage.Window;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

  private static final SceneManager sm = new SceneManager();
  private ObservableList<Customer> allCustomers;
  private static final String customersForm = "/fxml/customersForm.fxml";

  @FXML
  private TableView customersTable;

  /**
   * Initializer for login scene
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    CustomersService customersService = new CustomersService();
    try {
      allCustomers = customersService.getCustomers();
      setupCustomersTable(allCustomers, resourceBundle);
      // setCustomerTableDimensions();
    } catch (SQLException ex) {
      Database.printSQLException(ex);
    }
  }

  /**
   * method to print all the customer data
   * @param allCustomers - all the customers in the database
   */
  private void setupCustomersTable(ObservableList allCustomers, ResourceBundle resourceBundle) {

    // TODO: get labels from i18n
    TableColumn idCol = new TableColumn(resourceBundle.getString("customers_table.columns.id"));
    idCol.setMinWidth(10);
    idCol.setCellValueFactory(
      new PropertyValueFactory<Customer, String>("id"));

    TableColumn nameCol = new TableColumn(resourceBundle.getString("customers_table.columns.name"));
    nameCol.setMinWidth(100);
    nameCol.setCellValueFactory(
      new PropertyValueFactory<Customer, String>("name"));

    TableColumn addressCol = new TableColumn(resourceBundle.getString("customers_table.columns.address"));
    addressCol.setMinWidth(150);
    addressCol.setCellValueFactory(
      new PropertyValueFactory<Customer, String>("address"));

    TableColumn postalCodeCol = new TableColumn(resourceBundle.getString("customers_table.columns.postal_code"));
    postalCodeCol.setMinWidth(50);
    postalCodeCol.setCellValueFactory(
      new PropertyValueFactory<Customer, String>("postalCode"));

    TableColumn phoneCol = new TableColumn(resourceBundle.getString("customers_table.columns.phone"));
    phoneCol.setMinWidth(50);
    phoneCol.setCellValueFactory(
      new PropertyValueFactory<Customer, String>("phone"));

    TableColumn createDateCol = new TableColumn(resourceBundle.getString("customers_table.columns.created"));
    createDateCol.setMinWidth(50);
    createDateCol.setCellValueFactory(
      new PropertyValueFactory<Customer, Date>("createDate"));

    customersTable.setItems(allCustomers);
    customersTable.setMinSize(600, 400);
    customersTable.getColumns().addAll(
      nameCol,
      addressCol,
      postalCodeCol,
      phoneCol,
      createDateCol);
  }

  public void editCustomer(ActionEvent actionEvent) {
    // TODO: move to service
    try {
      Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
      Stage stage = new Stage();
      Parent root = FXMLLoader.load(
        CustomersController.class.getResource("/fxml/customerForm.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Edit Customer");
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(
        ((Node)actionEvent.getSource()).getScene().getWindow() );
      stage.show();
    } catch (Exception e) {
      Window owner = customersTable.getScene().getWindow();
      SceneManager.showAlert(Alert.AlertType.ERROR, owner, "Contacts Error!",
        "Could not edit contact: " + e.getMessage());
    }
  }

  public void createCustomer(ActionEvent actionEvent) {
  }
}
