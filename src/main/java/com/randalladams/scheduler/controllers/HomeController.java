package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.util.Lang;
import com.randalladams.scheduler.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController<CustomerTabPage, AppointmentTabPage> implements Initializable {
  @FXML
  private CustomerTabPage customerTabPage;
  @FXML
  private CustomerController customerController;

  @FXML
  private AppointmentTabPage appointmentTabPage;

  @FXML
  private AppointmentController appointmentController;

  private AppointmentService appointmentService;

  private static Alert errorAlert;
  private static Alert appointmentAlert;

  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentService = new AppointmentService();
    try {
      Appointment alertedAppointment = appointmentService.getAppointmentWithinFifteenMinutes(UserSession.getUserId());
      String appointmentAlertText = getAppointmentAlertText(alertedAppointment);
      setupAndShowAppointmentAlert(appointmentAlertText);
    } catch (SQLException e) {
      showErrorAlert(e.getMessage());
    }
  }

  public void setupAndShowAppointmentAlert(String appointmentAlertText) {
    appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
    appointmentAlert.setTitle("Upcoming appointment");
    appointmentAlert.setContentText(appointmentAlertText);
    appointmentAlert.showAndWait();
  }

  public void showErrorAlert(String alertText) {
    errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setTitle("error trying to get recent appointments");
    errorAlert.setContentText(alertText);
    errorAlert.show();
  }

  public String getAppointmentAlertText(Appointment appointment) {
    String appointmentAlertText = "";
    if (appointment != null) {
      appointmentAlertText = "There is an upcoming appointment:" +
        "\n Appointment ID: " + appointment.getAppointmentId() +
        "\n Appointment Start: " + appointment.getStartTimestamp();
    } else {
      appointmentAlertText = "There is no appointments within the next 15 minutes.";
    }
    return appointmentAlertText;
  }
}
