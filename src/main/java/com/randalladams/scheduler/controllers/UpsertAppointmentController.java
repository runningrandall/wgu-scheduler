package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.services.ContactService;
import com.randalladams.scheduler.util.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * UpsertAppointment controller that handles interactions for adding and/or updating appointments
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class UpsertAppointmentController implements Initializable {

  @FXML
  private Label appointmentIdLabel;
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
  private DateTimePicker appointmentStart;

  @FXML
  private DateTimePicker appointmentEnd;

  @FXML
  private TextField appointmentCustomerId;

  @FXML
  private TextField appointmentUserId;

  @FXML
  private ChoiceBox<KeyValuePair> contactsChoiceBox;

  @FXML
  private Button submitButton;

  @FXML
  private Button deleteButton;

  private static Boolean isNewAppointment;
  private static final ContactService contactService = new ContactService();
  private static final AppointmentService as = new AppointmentService();
  private static ObservableList<KeyValuePair> contacts;

  /**
   * Initializer for create/update customer scene
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      isNewAppointment = UserSession.getCurrentAppointmentSelected() == 0;
      contacts = contactService.getContactKeyValuePairs();
      contactsChoiceBox.setItems(contacts);
      if (!isNewAppointment) {
        populateAppointment();
      } else {
        hideNewFields();
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * method to hide fields not used when creating
   */
  private void hideNewFields() {
    appointmentIdLabel.setVisible(false);
    appointmentId.setVisible(false);
    deleteButton.setVisible(false);
  }

  /**
   * Method to populate the appointment form when updating
   * @throws SQLException - sql error
   */
  private void populateAppointment() throws SQLException {
    Appointment appointment = as.getAppointmentById(UserSession.getCurrentAppointmentSelected());
    KeyValuePair contactKvp = new KeyValuePair(String.valueOf(appointment.getContactId()), appointment.getContactName());
    appointmentId.setText(String.valueOf(appointment.getAppointmentId()));
    appointmentId.setDisable(true);
    appointmentTitle.setText(appointment.getTitle());
    appointmentDescription.setText(appointment.getDescription());
    appointmentLocation.setText(appointment.getLocation());
    appointmentType.setText(appointment.getType());
    appointmentStart.setDateTimeValue(Database.getZonedDateTimeFromDbDate(appointment.getStart()));
    appointmentEnd.setDateTimeValue(Database.getZonedDateTimeFromDbDate(appointment.getEnd()));
    appointmentCustomerId.setText(String.valueOf(appointment.getCustomerId()));
    appointmentUserId.setText(String.valueOf(appointment.getUserId()));
    contactsChoiceBox.setValue(contactKvp);
    contactsChoiceBox.getSelectionModel().select(getSelectedContactIndex(contactKvp));
  }

  /**
   * method to get the proper index for selecting the right contact when updating
   * @param contactKvp - the contact key value pair
   * @return int
   */
  private int getSelectedContactIndex(KeyValuePair contactKvp) {
    AtomicInteger foundIndex = new AtomicInteger(-1);
    IntStream.range(0, contacts.size()).forEach(i -> {
      if (contacts.get(i).getKey().equals(contactKvp.getKey())) {
        foundIndex.set(i);
      }
    });
    return foundIndex.get();
  }

  /**
   * method to submit the appointment for creating/updating
   * the method first checks for appointment validity and then updates/creates
   */
  public void submitAppointment() {
    Validator appointValidity;
    ZonedDateTime zonedStartDateTime = Database.getEstFromZoneLocalDateTime(appointmentStart.getDateTimeValue());
    ZonedDateTime zonedEndDateTime = Database.getEstFromZoneLocalDateTime(appointmentEnd.getDateTimeValue());
    Alert errorAlert;
    try {
      appointValidity = as.validateAppointment(
        isNewAppointment,
        appointmentTitle.getText(),
        appointmentDescription.getText(),
        appointmentLocation.getText(),
        appointmentType.getText(),
        zonedStartDateTime,
        zonedEndDateTime,
        Integer.parseInt(appointmentCustomerId.getText()),
        Integer.parseInt(appointmentUserId.getText()),
        Integer.parseInt(contactsChoiceBox.getValue().getKey())
      );
    } catch (Exception e) {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("appointment_form.error.title"));
      errorAlert.setContentText(e.getMessage());
      errorAlert.show();
      return;
    }

    if (appointValidity.getValid()) {
      if (!isNewAppointment) {
        try {
          as.updateAppointment(
            Integer.parseInt(appointmentId.getText()),
            appointmentTitle.getText(),
            appointmentDescription.getText(),
            appointmentLocation.getText(),
            appointmentType.getText(),
            Database.getUtcDateTimeFromEstDateTime(appointmentStart.getDateTimeValue()),
            Database.getUtcDateTimeFromEstDateTime(appointmentEnd.getDateTimeValue()),
            Integer.parseInt(appointmentCustomerId.getText()),
            Integer.parseInt(appointmentUserId.getText()),
            Integer.parseInt(contactsChoiceBox.getValue().getKey())
          );
        } catch (SQLException e) {
          errorAlert = new Alert(Alert.AlertType.ERROR);
          errorAlert.setTitle(Lang.getString("appointment_form.error.title"));
          errorAlert.setContentText(e.getMessage());
          errorAlert.show();
          return;
        }
      } else {
        try {
          as.createAppointment(
            appointmentTitle.getText(),
            appointmentDescription.getText(),
            appointmentLocation.getText(),
            appointmentType.getText(),
            Database.getUtcDateTimeFromEstDateTime(appointmentStart.getDateTimeValue()),
            Database.getUtcDateTimeFromEstDateTime(appointmentEnd.getDateTimeValue()),
            Integer.parseInt(appointmentCustomerId.getText()),
            Integer.parseInt(appointmentUserId.getText()),
            Integer.parseInt(contactsChoiceBox.getValue().getKey())
          );
        } catch (SQLException e) {
          errorAlert = new Alert(Alert.AlertType.ERROR);
          errorAlert.setTitle(Lang.getString("appointment_form.error.title"));
          errorAlert.setContentText(e.getMessage());
          errorAlert.show();
          return;
        }
      }
    } else {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("appointment_form.error.title"));
      errorAlert.setContentText(appointValidity.getMessage());
      errorAlert.show();
      return;
    }

    Stage stage = (Stage) submitButton.getScene().getWindow();
    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }

  /**
   * method to confirm the delete request
   * users must confirm before deleting so appointments
   * are not deleted by accident
   */
  public void confirmAppointmentDelete() {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle(Lang.getString("appointment_form.delete.title"));
    confirmationAlert.setContentText(Lang.getString("appointment_form.delete.text"));
    confirmationAlert.showAndWait();
    if (confirmationAlert.getResult() == ButtonType.OK) {
      try {
        AppointmentService.deleteAppointmentById(UserSession.getCurrentAppointmentSelected());
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
      } catch (Exception e) {
        System.out.println("Error deleting appointment" + e.getMessage());
      }
    }
  }
}
