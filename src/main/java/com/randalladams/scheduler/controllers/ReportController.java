package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.model.ReportAppointmentType;
import com.randalladams.scheduler.services.AppointmentService;
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



public class ReportController implements Initializable {

  @FXML
  private ChoiceBox<KeyValuePair> reportsChoiceBox;

  @FXML
  private TableView<ReportAppointmentType> reportAppointmentTypeTable;

  private ResourceBundle langBundle;
  private AppointmentService appointmentService = new AppointmentService();
  private static Alert errorAlert;
  private String lang = System.getProperty("user.language");
  private Locale locale = new Locale(lang, lang.toUpperCase());

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    langBundle = ResourceBundle.getBundle("i18n", locale);
    setupReportsChoiceBox();
    reportAppointmentTypeTable.setVisible(false);
  }

  private void setupReportsChoiceBox() {
    ObservableList availableReports = FXCollections.observableArrayList(
      new KeyValuePair("1", langBundle.getString("reports.report_1")),
      new KeyValuePair("2", langBundle.getString("reports.report_2")),
      new KeyValuePair("3", langBundle.getString("reports.report_3"))
    );

    reportsChoiceBox.setItems(availableReports);
  }

  @FXML
  private void runReport(ActionEvent event) {
    switch(reportsChoiceBox.getSelectionModel().getSelectedIndex()) {
      case 0:
        printAppointmentsByTypeAndMonth();
        break;
      case 1:
        System.out.println("bar");
        break;
      case 2:
        System.out.println("baz");
        break;
      default:
        System.out.println("No report found");
        break;
    }
  }

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

  private void showErrorAlert(String message) {
    errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setTitle(Lang.getString("reports.error.title"));
    errorAlert.setContentText(message);
    errorAlert.show();
  }
}
