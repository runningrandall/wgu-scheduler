package com.randalladams.scheduler.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class MainApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    // get and set the locale
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    FXMLLoader fxmlLoader = new FXMLLoader();
    Parent root = fxmlLoader.load(getClass().getResource("/fxml/login.fxml"), bundle);
    stage.setTitle("Login");
    stage.setScene(new Scene(root, 800, 500));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}