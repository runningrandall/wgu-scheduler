package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.services.AppointmentService;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

  private static ResourceBundle resourceBundle = null;
  private static ResourceBundle langBundle = null;
  private static AppointmentService appointmentService;
  private static final String appointmentForm = "/fxml/appointmentForm.fxml";

  @FXML
  private TableView<Appointment> appointmentsTable;

  /**
   * Initializer for login scene
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentService = new AppointmentService();
    AppointmentController.resourceBundle = resourceBundle;
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    langBundle = ResourceBundle.getBundle("i18n", locale);
    try {
      ObservableList<Appointment> allAppointments = appointmentService.getAppointmentsByUserId(UserSession.getUserId());
      setupAppointmentsTable(allAppointments, resourceBundle);
    } catch (SQLException ex) {
      Database.printSQLException(ex);
    }
  }

  public void editAppointment(ActionEvent actionEvent) {
    try {
      Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
      System.out.println(selectedAppointment.getAppointmentId());
      UserSession.setCurrentAppointmentSelected(selectedAppointment.getAppointmentId());
      Stage stage = new Stage();
      Parent root = FXMLLoader.load(
        AppointmentController.class.getResource(appointmentForm), resourceBundle);
      stage.setScene(new Scene(root, 820, 520));
      stage.setTitle(langBundle.getString("appointment_form.edit_title"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(
        ((Node) actionEvent.getSource()).getScene().getWindow());

      stage.setOnCloseRequest(e -> {
        ObservableList<Appointment> allAppointments = null;
        try {
          allAppointments = appointmentService.getAppointmentsByUserId(UserSession.getUserId());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
        setupAppointmentsTable(allAppointments, resourceBundle);
      });
      stage.show();
    } catch (Exception e) {
      SceneManager.showAlert(Alert.AlertType.ERROR, appointmentsTable.getScene().getWindow(), langBundle.getString("errors.missing_appointment_selection.text"),
        langBundle.getString("errors.missing_appointment_selection.text") + e.getMessage());
    }
  }

  public void createAppointment(ActionEvent actionEvent) {
    System.out.println("create");
  }

  private void setupAppointmentsTable(ObservableList<Appointment> appointments, ResourceBundle resourceBundle) {

    TableColumn<Appointment, String> idCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.id"));
    idCol.setMinWidth(10);
    idCol.setCellValueFactory(
      new PropertyValueFactory<>("appointmentId"));

    TableColumn<Appointment, String> titleCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.title"));
    titleCol.setMinWidth(10);
    titleCol.setCellValueFactory(
      new PropertyValueFactory<>("title"));

    TableColumn<Appointment, String> descriptionCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.description"));
    descriptionCol.setMinWidth(10);
    descriptionCol.setCellValueFactory(
      new PropertyValueFactory<>("description"));

    TableColumn<Appointment, String> locationCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.location"));
    locationCol.setMinWidth(10);
    locationCol.setCellValueFactory(
      new PropertyValueFactory<>("location"));

    TableColumn<Appointment, String> contactCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.contact"));
    contactCol.setMinWidth(10);
    contactCol.setCellValueFactory(
      new PropertyValueFactory<>("contactName"));

    TableColumn<Appointment, String> typeCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.type"));
    typeCol.setMinWidth(10);
    typeCol.setCellValueFactory(
      new PropertyValueFactory<>("type"));

    TableColumn<Appointment, String> startCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.start"));
    startCol.setMinWidth(10);
    startCol.setCellValueFactory(
      new PropertyValueFactory<>("start"));

    TableColumn<Appointment, String> endCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.end"));
    endCol.setMinWidth(10);
    endCol.setCellValueFactory(
      new PropertyValueFactory<>("end"));

    TableColumn<Appointment, String> customerIdCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.customer_id"));
    customerIdCol.setMinWidth(10);
    customerIdCol.setCellValueFactory(
      new PropertyValueFactory<>("customerId"));

    TableColumn<Appointment, String> userIdCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.user_id"));
    userIdCol.setMinWidth(10);
    userIdCol.setCellValueFactory(
      new PropertyValueFactory<>("userId"));

    appointmentsTable.setItems(appointments);
    appointmentsTable.setMinSize(800, 400);
    appointmentsTable.getColumns().addAll(
      idCol,
      titleCol,
      descriptionCol,
      locationCol,
      contactCol,
      typeCol,
      startCol,
      endCol,
      customerIdCol,
      userIdCol
    );
  }
}
