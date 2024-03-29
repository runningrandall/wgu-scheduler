package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.Lang;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Appointment controller adds the functionality for interacting with the appointment list
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class AppointmentController implements Initializable {

  private static ResourceBundle resourceBundle = null;
  private static ResourceBundle langBundle = null;
  private static AppointmentService appointmentService;
  private static final String appointmentForm = "/fxml/appointmentForm.fxml";
  private static Alert errorAlert;

  @FXML
  private TableView<Appointment> appointmentsTable;

  @FXML
  private SplitMenuButton filterButton;

  private static final int MODAL_WIDTH = 800;
  private static final int MODAL_HEIGHT = 600;

  /**
   * Initializer for appointment table
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentService = new AppointmentService();
    AppointmentController.resourceBundle = resourceBundle;
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    langBundle = ResourceBundle.getBundle("i18n", locale);
    setupFilterMenu();
    try {
      ObservableList<Appointment> allAppointments = appointmentService.getAppointmentsByUserId(UserSession.getUserId());
      setupAppointmentsTable(allAppointments, resourceBundle);
    } catch (SQLException ex) {
      Database.printSQLException(ex);
    }
  }

  /**
   * Method to load the edit appointment form
   * <p>
   * if the appointment cannot load an alert error will appear
   * </p>
   * @param actionEvent
   */
  public void editAppointment(ActionEvent actionEvent) {
    try {
      Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
      UserSession.setCurrentAppointmentSelected(selectedAppointment.getAppointmentId());
      Stage stage = new Stage();
      Parent root = FXMLLoader.load(
        AppointmentController.class.getResource(appointmentForm), resourceBundle);
      stage.setScene(new Scene(root, MODAL_WIDTH, MODAL_HEIGHT));
      stage.setTitle(langBundle.getString("appointment_form.edit_title"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(
        ((Node) actionEvent.getSource()).getScene().getWindow());

      stage.setOnCloseRequest(e -> {
        ObservableList<Appointment> allAppointments = null;
        try {
          allAppointments = appointmentService.getAppointmentsByUserId(UserSession.getUserId());
        } catch (SQLException ex) {
          showErrorAlert(ex.getMessage());
        }
        setupAppointmentsTable(allAppointments, resourceBundle);
      });
      stage.show();
    } catch (Exception e) {
      showErrorAlert(langBundle.getString("errors.missing_appointment_selection.text") + e.getMessage());
    }
  }

  /**
   * method to load the create appointment form
   * <p>lambda expression</p>
   * <p>
   *   the lambda expression used here adds an event listener with the form closes
   *   so that the table can be redrawn
   * </p>
   * @param actionEvent
   */
  public void createAppointment(ActionEvent actionEvent) {
    try {
      UserSession.setCurrentAppointmentSelected(0);
      Stage stage = new Stage();
      Parent root = FXMLLoader.load(
        AppointmentController.class.getResource(appointmentForm), resourceBundle);
      stage.setScene(new Scene(root, MODAL_WIDTH, MODAL_HEIGHT));
      stage.setTitle(langBundle.getString("appointment_form.create_title"));
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
      showErrorAlert(e.getMessage());
    }
  }

  /**
   * method to setup the appointments table
   * the appointments table setup is taking appointment data and constructing a table view
   * @param appointments
   * @param resourceBundle
   */
  private void setupAppointmentsTable(ObservableList<Appointment> appointments, ResourceBundle resourceBundle) {
    appointmentsTable.getColumns().clear();
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
      new PropertyValueFactory<>("startDisplayString"));

    TableColumn<Appointment, String> endCol = new TableColumn<>(resourceBundle.getString("appointment_table.columns.end"));
    endCol.setMinWidth(10);
    endCol.setCellValueFactory(
      new PropertyValueFactory<>("endDisplayString"));

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

  /**
   * the setup filter menu method creates the menu items for filtering the appointments table
   * <p>Lambda Expression</p>
   * <p>There are lambdas in here to add event listeners for detecting menu filter changes</p>
   */
  private void setupFilterMenu() {
    MenuItem allAppointments = new MenuItem(langBundle.getString("appointment_form.filter.all"));
    MenuItem monthAppointments = new MenuItem(langBundle.getString("appointment_form.filter.month"));
    MenuItem weekAppointments = new MenuItem(langBundle.getString("appointment_form.filter.week"));
    filterButton.getItems().addAll(allAppointments, monthAppointments, weekAppointments);

    allAppointments.setOnAction((e)-> {
      try {
        ObservableList<Appointment> appointments = appointmentService.getAppointmentsByUserId(UserSession.getUserId());
        setupAppointmentsTable(appointments, AppointmentController.resourceBundle);
      } catch (Exception ex) {
        showErrorAlert(ex.getMessage());
      }
    });
    monthAppointments.setOnAction((e)-> {
      try {
        ObservableList<Appointment> appointments = appointmentService.getAppointmentsByWeekOrMonth(UserSession.getUserId(), "MONTH");
        setupAppointmentsTable(appointments, AppointmentController.resourceBundle);
      } catch (Exception ex) {
        showErrorAlert(ex.getMessage());
      }
    });
    weekAppointments.setOnAction((e)-> {
      try {
        ObservableList<Appointment> appointments = appointmentService.getAppointmentsByWeekOrMonth(UserSession.getUserId(), "WEEK");
        setupAppointmentsTable(appointments, AppointmentController.resourceBundle);
      } catch (Exception ex) {
        showErrorAlert(ex.getMessage());
      }
    });
  }

  /**
   * Generic method to show an alert when something goes wrong
   * @param message
   */
  private void showErrorAlert(String message) {
    errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setTitle(Lang.getString("appointments_table.error.title"));
    errorAlert.setContentText(message);
    errorAlert.show();
  }
}
