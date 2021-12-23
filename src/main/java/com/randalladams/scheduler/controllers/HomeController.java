package com.randalladams.scheduler.controllers;

import javafx.fxml.FXML;

public class HomeController<CustomerTabPage, AppointmentTabPage> {
  @FXML
  private CustomerTabPage customerTabPage;
  @FXML
  private CustomerController customerController;

  @FXML
  private AppointmentTabPage appointmentTabPage;

  @FXML
  private AppointmentController appointmentController;
}
