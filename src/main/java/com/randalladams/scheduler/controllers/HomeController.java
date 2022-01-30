package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Home controller for controlling the home scene
 * The home scene is the default upon login. It has a tab view for each core app function
 * @param <CustomerTabPage>
 * @param <AppointmentTabPage>
 * @param <ReportTabPage>
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class HomeController<CustomerTabPage, AppointmentTabPage, ReportTabPage> implements Initializable {
  @FXML
  private CustomerTabPage customerTabPage;
  @FXML
  private CustomerController customerController;

  @FXML
  private AppointmentTabPage appointmentTabPage;

  @FXML
  private AppointmentController appointmentController;

  @FXML
  private ReportTabPage reportTabPage;

  @FXML
  private ReportController reportController;

  private AppointmentService appointmentService;

  private static Alert errorAlert;
  private static Alert appointmentAlert;
  private static ResourceBundle langBundle = null;

  /**
   * initializer
   * @param url
   * @param resourceBundle
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentService = new AppointmentService();
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    langBundle = ResourceBundle.getBundle("i18n", locale);
    try {
      Appointment alertedAppointment = appointmentService.getAppointmentWithinFifteenMinutes(UserSession.getUserId());
      String appointmentAlertText = getAppointmentAlertText(alertedAppointment);
      setupAndShowAppointmentAlert(appointmentAlertText);
    } catch (SQLException e) {
      showErrorAlert(e.getMessage());
    }
  }

  /**
   * shows our appointment alert in a JavaFX Alert
   * @param appointmentAlertText
   */
  public void setupAndShowAppointmentAlert(String appointmentAlertText) {
    appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
    appointmentAlert.setTitle(langBundle.getString("appointment_alert.title"));
    appointmentAlert.setContentText(appointmentAlertText);
    appointmentAlert.showAndWait();
  }

  /**
   * Generic method to show an alert error when something goes wrong
   * @param alertText
   */
  public void showErrorAlert(String alertText) {
    errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setTitle(langBundle.getString("appointment_alert.error"));
    errorAlert.setContentText(alertText);
    errorAlert.show();
  }

  /**
   * Method to get the appointment alert text (when there is and isn't an appointment)
   * @param appointment
   * @return String
   */
  public String getAppointmentAlertText(Appointment appointment) {
    String appointmentAlertText = "";
    if (appointment != null) {
      appointmentAlertText = langBundle.getString("appointment_alert.text_1") +
        "\n " + langBundle.getString("appointment_alert.text_2") + " " + appointment.getAppointmentId() +
        "\n " + langBundle.getString("appointment_alert.text_3") + " " + appointment.getStarTime();
    } else {
      appointmentAlertText = langBundle.getString("appointment_alert.no_appointment");
    }
    return appointmentAlertText;
  }
}
