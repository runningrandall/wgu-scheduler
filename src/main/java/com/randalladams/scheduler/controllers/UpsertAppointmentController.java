package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.Customer;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.services.ContactService;
import com.randalladams.scheduler.util.KeyValuePair;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
}
