package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.Login;
import com.randalladams.scheduler.model.ReportAppointmentType;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.services.LoginService;
import com.randalladams.scheduler.util.KeyValuePair;
import com.randalladams.scheduler.util.Lang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Report controller for handling the report scene
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class ReportController implements Initializable {

  @FXML
  private ChoiceBox<KeyValuePair> reportsChoiceBox;

  @FXML
  private TableView<ReportAppointmentType> reportAppointmentTypeTable;

  @FXML
  private TableView<Appointment> reportContactSchedule;

  @FXML
  private TableView<Login> reportLoginActivity;

  private ResourceBundle langBundle;
  private AppointmentService appointmentService = new AppointmentService();
  private LoginService loginService = new LoginService();
  private static Alert errorAlert;
  private String lang = System.getProperty("user.language");
  private Locale locale = new Locale(lang, lang.toUpperCase());

  /**
   * initializer
   * @param url
   * @param resourceBundle
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    langBundle = ResourceBundle.getBundle("i18n", locale);
    setupReportsChoiceBox();
    hideAllTables();
  }

  /**
   * Method to hide all of our reports by default
   */
  private void hideAllTables() {
    reportAppointmentTypeTable.setVisible(false);
    reportContactSchedule.setVisible(false);
    reportLoginActivity.setVisible(false);
  }

  /**
   * method to setup the various reports to be selected in a choice box
   */
  private void setupReportsChoiceBox() {
    ObservableList availableReports = FXCollections.observableArrayList(
      new KeyValuePair("1", langBundle.getString("reports.report_1")),
      new KeyValuePair("2", langBundle.getString("reports.report_2")),
      new KeyValuePair("3", langBundle.getString("reports.report_3"))
    );

    reportsChoiceBox.setItems(availableReports);
  }

  /**
   * method to handle the run report button click
   * based on what report was selected the method will call
   * the proper report gather/setup method
   * and show an alert otherwise
   * @param event
   */
  @FXML
  private void runReport(ActionEvent event) {
    hideAllTables();
    switch(reportsChoiceBox.getSelectionModel().getSelectedIndex()) {
      case 0:
        printAppointmentsByTypeAndMonth();
        break;
      case 1:
        printContactSchedule();
        break;
      case 2:
        printLoginActivity();
        break;
      default:
        showErrorAlert("Please select a report");
        break;
    }
  }

  /**
   * Method to get a report of appointments by type and month
   * show the proper table and then print the appointments in table form
   */
  private void printAppointmentsByTypeAndMonth() {
    reportAppointmentTypeTable.setVisible(true);
    try {
      ObservableList<ReportAppointmentType> reportList = appointmentService.getAppointmentsByTypeAndMonth();
      reportAppointmentTypeTable.getColumns().clear();

      TableColumn<ReportAppointmentType, String> typeCol = new TableColumn<>(langBundle.getString("reports_table.columns.type"));
      typeCol.setCellValueFactory(
        new PropertyValueFactory<>("type"));

      TableColumn<ReportAppointmentType, String> monthYearCol = new TableColumn<>(langBundle.getString("reports_table.columns.month"));
      monthYearCol.setCellValueFactory(
        new PropertyValueFactory<>("monthYear"));

      TableColumn<ReportAppointmentType, String> totalCol = new TableColumn<>(langBundle.getString("reports_table.columns.count"));
      totalCol.setCellValueFactory(
        new PropertyValueFactory<>("total"));

      reportAppointmentTypeTable.setItems(reportList);
      reportAppointmentTypeTable.getColumns().addAll(
        typeCol,
        monthYearCol,
        totalCol
      );
    } catch (Exception e) {
      showErrorAlert(e.getMessage());
    }
  }

  /**
   * method to print a schedule for each contact
   */
  private void printContactSchedule() {
    reportContactSchedule.setVisible(true);
    try {
      ObservableList<Appointment> reportList = appointmentService.getContactsAppointmentSchedule();

      TableColumn<Appointment, String> contactNameCol = new TableColumn<>(langBundle.getString("reports_table.columns.contact_name"));
      contactNameCol.setCellValueFactory(
        new PropertyValueFactory<>("contactName"));

      TableColumn<Appointment, String> appointmentIdCol = new TableColumn<>(langBundle.getString("reports_table.columns.appointment_id"));
      appointmentIdCol.setCellValueFactory(
        new PropertyValueFactory<>("appointmentId"));

      TableColumn<Appointment, String> titleCol = new TableColumn<>(langBundle.getString("reports_table.columns.title"));
      titleCol.setCellValueFactory(
        new PropertyValueFactory<>("title"));

      TableColumn<Appointment, String> typeCol = new TableColumn<>(langBundle.getString("reports_table.columns.type"));
      typeCol.setCellValueFactory(
        new PropertyValueFactory<>("type"));

      TableColumn<Appointment, String> descriptionCol = new TableColumn<>(langBundle.getString("reports_table.columns.description"));
      descriptionCol.setCellValueFactory(
        new PropertyValueFactory<>("description"));

      TableColumn<Appointment, String> startCol = new TableColumn<>(langBundle.getString("reports_table.columns.start"));
      startCol.setCellValueFactory(
        new PropertyValueFactory<>("startDisplayString"));

      TableColumn<Appointment, String> endCol = new TableColumn<>(langBundle.getString("reports_table.columns.end"));
      endCol.setCellValueFactory(
        new PropertyValueFactory<>("endDisplayString"));

      TableColumn<Appointment, String> customerIdCol = new TableColumn<>(langBundle.getString("reports_table.columns.customer_id"));
      customerIdCol.setCellValueFactory(
        new PropertyValueFactory<>("customerId"));

      reportContactSchedule.setItems(reportList);
      reportContactSchedule.getColumns().addAll(
        contactNameCol,
        appointmentIdCol,
        titleCol,
        typeCol,
        descriptionCol,
        startCol,
        endCol,
        customerIdCol
      );
    } catch (Exception e) {
      showErrorAlert(e.getMessage());
    }

  }

  /**
   * method to print the login activity in a table
   */
  private void printLoginActivity() {
    reportLoginActivity.setVisible(true);
    try {
      ObservableList<Login> loginData = loginService.getLoginReport();
      reportLoginActivity.getColumns().clear();

      TableColumn<Login, String> usernameCol = new TableColumn<>(langBundle.getString("reports_table.columns.username"));
      usernameCol.setCellValueFactory(
        new PropertyValueFactory<>("username"));

      TableColumn<Login, String> loginDateCol = new TableColumn<>(langBundle.getString("reports_table.columns.login_date"));
      loginDateCol.setCellValueFactory(
        new PropertyValueFactory<>("loginDate"));

      TableColumn<Login, String> statusCol = new TableColumn<>(langBundle.getString("reports_table.columns.login_status"));
      statusCol.setCellValueFactory(
        new PropertyValueFactory<>("status"));

      reportLoginActivity.setItems(loginData);
      reportLoginActivity.getColumns().addAll(
        usernameCol,
        loginDateCol,
        statusCol
      );
    } catch (Exception e) {
      showErrorAlert(e.getMessage());
    }
  }

  /**
   * generic method to show an alert if something goes wrong
   * @param message
   */
  private void showErrorAlert(String message) {
    errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setTitle(Lang.getString("reports.error.title"));
    errorAlert.setContentText(message);
    errorAlert.show();
  }
}
