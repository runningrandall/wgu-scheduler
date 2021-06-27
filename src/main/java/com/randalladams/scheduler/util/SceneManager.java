package com.randalladams.scheduler.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneManager {
  public void setScene(String fxml, String title, int width, int height) throws IOException {
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    FXMLLoader fxmlLoader = new FXMLLoader();
    Parent scene = fxmlLoader.load(getClass().getResource(fxml), bundle);
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.setScene(new Scene(scene, width, height));
    stage.show();
  }
}
