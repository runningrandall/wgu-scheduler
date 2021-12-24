package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.services.ContactService;
import com.randalladams.scheduler.services.CustomerService;
import com.randalladams.scheduler.util.KeyValuePair;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpsertAppointmentController implements Initializable {

  @FXML
  private TextField appointmentId;

  @FXML
  private TextField appointmentTitle;

  @FXML
  private TextArea appointmentDescription;

  @FXML
  private TextField appointmentLocation;

  @FXML
  private TextField appointmentType;

  @FXML
  private DatePicker appointmentStart;

  @FXML
  private DatePicker appointmentEnd;

  @FXML
  private TextField appointmentCustomerId;

  @FXML
  private TextField appointmentUserId;

  @FXML
  private ChoiceBox<KeyValuePair> contactsChoiceBox;

  @FXML
  private Button submitButton;

  private static Appointment appointment;
  private static Boolean isNewAppointment;
  private static final ContactService contactService = new ContactService();

  /**
   * Initializer for create customer modal
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      isNewAppointment = UserSession.getCurrentAppointmentSelected() == 0;
      ObservableList<KeyValuePair> contacts = contactService.getContactKeyValuePairs();
      contactsChoiceBox.setItems(contacts);

      if (!isNewAppointment) {
        appointment = AppointmentService.getAppointmentById(UserSession.getCurrentAppointmentSelected());
        KeyValuePair contactKvp = new KeyValuePair(String.valueOf(appointment.getContactId()), appointment.getContactName());
        appointmentId.setText(String.valueOf(appointment.getAppointmentId()));
        appointmentId.setDisable(true);
        appointmentTitle.setText(appointment.getTitle());
        appointmentDescription.setText(appointment.getDescription());
        appointmentLocation.setText(appointment.getLocation());
        appointmentType.setText(appointment.getType());
        appointmentStart.setValue(appointment.getStartTimestamp().toLocalDateTime().toLocalDate());
        appointmentEnd.setValue(appointment.getEndTimestamp().toLocalDateTime().toLocalDate());
        appointmentCustomerId.setText(String.valueOf(appointment.getCustomerId()));
        appointmentUserId.setText(String.valueOf(appointment.getUserId()));
        contactsChoiceBox.setValue(contactKvp);
      } else {
        appointmentId.setVisible(false);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void submitAppointment(ActionEvent event) throws SQLException {
    // TODO: add logic
    Boolean isAppointmentValid = AppointmentService.validateAppointment();
    if (!isNewAppointment) {
      // edit
      Appointment appointment = AppointmentService.updateAppointment(
        Integer.parseInt(appointmentId.getText()),
        appointmentTitle.getText(),
        appointmentDescription.getText(),
        appointmentLocation.getText(),
        appointmentType.getText(),
        java.sql.Date.valueOf(appointmentStart.getValue()),
        java.sql.Date.valueOf(appointmentEnd.getValue()),
        Integer.parseInt(appointmentCustomerId.getText()),
        Integer.parseInt(appointmentUserId.getText()),
        Integer.parseInt(contactsChoiceBox.getValue().getKey())
      );
      Stage stage = (Stage) submitButton.getScene().getWindow();
      stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    } else {
      // create
      // Appointment = AppointmentService.createAppointment()
    }
  }
}
