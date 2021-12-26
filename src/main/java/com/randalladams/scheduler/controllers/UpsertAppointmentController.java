package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.services.AppointmentService;
import com.randalladams.scheduler.services.ContactService;
import com.randalladams.scheduler.util.KeyValuePair;
import com.randalladams.scheduler.util.Lang;
import com.randalladams.scheduler.util.UserSession;
import com.randalladams.scheduler.util.Validator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tornadofx.control.DateTimePicker;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

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

  private static Appointment appointment;
  private static Boolean isNewAppointment;
  private static final ContactService contactService = new ContactService();
  private static Alert confirmationAlert;
  private static Alert errorAlert;
  private static AppointmentService as = new AppointmentService();

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
        appointment = as.getAppointmentById(UserSession.getCurrentAppointmentSelected());
        KeyValuePair contactKvp = new KeyValuePair(String.valueOf(appointment.getContactId()), appointment.getContactName());
        appointmentId.setText(String.valueOf(appointment.getAppointmentId()));
        appointmentId.setDisable(true);
        appointmentTitle.setText(appointment.getTitle());
        appointmentDescription.setText(appointment.getDescription());
        appointmentLocation.setText(appointment.getLocation());
        appointmentType.setText(appointment.getType());
        appointmentStart.setValue(LocalDate.from(appointment.getStartTimestamp().toLocalDateTime()));
        appointmentEnd.setValue(LocalDate.from(appointment.getEndTimestamp().toLocalDateTime()));
        appointmentCustomerId.setText(String.valueOf(appointment.getCustomerId()));
        appointmentUserId.setText(String.valueOf(appointment.getUserId()));
        contactsChoiceBox.setValue(contactKvp);
      } else {
        appointmentIdLabel.setVisible(false);
        appointmentId.setVisible(false);
        deleteButton.setVisible(false);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void submitAppointment(ActionEvent event) throws SQLException {
    Validator appointValidity = null;
    try {
      appointValidity = as.validateAppointment(
        appointmentTitle.getText(),
        appointmentDescription.getText(),
        appointmentLocation.getText(),
        appointmentType.getText(),
        appointmentStart.getDateTimeValue(),
        appointmentEnd.getDateTimeValue(),
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
        // edit
        as.updateAppointment(
          Integer.parseInt(appointmentId.getText()),
          appointmentTitle.getText(),
          appointmentDescription.getText(),
          appointmentLocation.getText(),
          appointmentType.getText(),
          appointmentStart.getDateTimeValue(),
          appointmentEnd.getDateTimeValue(),
          Integer.parseInt(appointmentCustomerId.getText()),
          Integer.parseInt(appointmentUserId.getText()),
          Integer.parseInt(contactsChoiceBox.getValue().getKey())
        );
      } else {
        as.createAppointment(
          appointmentTitle.getText(),
          appointmentDescription.getText(),
          appointmentLocation.getText(),
          appointmentType.getText(),
          appointmentStart.getDateTimeValue(),
          appointmentEnd.getDateTimeValue(),
          Integer.parseInt(appointmentCustomerId.getText()),
          Integer.parseInt(appointmentUserId.getText()),
          Integer.parseInt(contactsChoiceBox.getValue().getKey())
        );
      }
    } else {
      errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle(Lang.getString("appointment_form.error.title"));
      errorAlert.setContentText(appointValidity.getMessage());
      errorAlert.show();
    }

    Stage stage = (Stage) submitButton.getScene().getWindow();
    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }

  public void confirmAppointmentDelete(ActionEvent event) {
    confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle(Lang.getString("appointment_form.delete.title"));
    confirmationAlert.setContentText(Lang.getString("appointment_form.delete.text"));
    confirmationAlert.showAndWait();
    if (confirmationAlert.getResult() == ButtonType.OK) {
      try {
        as.deleteAppointmentById(UserSession.getCurrentAppointmentSelected());
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
      } catch (Exception e) {
        System.out.println("Error deleting appointment" + e.getMessage());
      }
    }
  }
}
