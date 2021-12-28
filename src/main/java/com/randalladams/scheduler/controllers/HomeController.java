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
    // determine if we can have any appointments
    appointmentService = new AppointmentService();
    try {
      Appointment alertedAppointment = appointmentService.getAppointmentWithinFifteenMinutes(UserSession.getUserId());
      appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
      appointmentAlert.setTitle("Upcoming appointment");
      if (alertedAppointment != null) {
        appointmentAlert.setContentText(
          "There is an upcoming appointment:" +
          "\n Appointment ID: " + alertedAppointment.getAppointmentId() +
          "\n Appointment Start: " + alertedAppointment.getStartTimestamp()
        );
      } else {
        appointmentAlert.setContentText("There is no appointments within the next 15 minutes.");
      }
      appointmentAlert.showAndWait();
    } catch (SQLException e) {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle("error trying to get recent appointments");
      errorAlert.setContentText(e.getMessage());
      errorAlert.show();
    }
  }
}
