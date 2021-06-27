package com.randalladams.scheduler.login;

import com.randalladams.scheduler.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class MainApp extends Application {

  private SceneManager sm = new SceneManager();
  @Override
  public void start(Stage stage) throws Exception {
    sm.setScene("/fxml/login.fxml", "Login", 800, 500);
  }

  public static void main(String[] args) {
    launch(args);
  }
}