package com.randalladams.scheduler.controllers;

import com.randalladams.scheduler.util.KeyValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;



public class ReportController implements Initializable {

  @FXML
  private ChoiceBox<KeyValuePair> reportsChoiceBox;

  private ResourceBundle langBundle;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    langBundle = ResourceBundle.getBundle("i18n", locale);

    ObservableList availableReports = FXCollections.observableArrayList(
      new KeyValuePair("1", langBundle.getString("reports.report_1")),
      new KeyValuePair("2", langBundle.getString("reports.report_2")),
      new KeyValuePair("3", langBundle.getString("reports.report_3"))
    );

    reportsChoiceBox.setItems(availableReports);
    reportsChoiceBox.setOnAction(e -> {
      System.out.println(e.toString());
    });
  }

  @FXML
  private void runReport(ActionEvent event) {
    System.out.println("run report");
    System.out.println(reportsChoiceBox.getValue());
    System.out.println(reportsChoiceBox.getSelectionModel().getSelectedIndex());
    System.out.println(reportsChoiceBox.getItems());
  }
}
