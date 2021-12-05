package com.randalladams.scheduler.util;
/*
 * SceneManager is a class for managing presentation of scenes
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2021
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneManager {

  private static final int defaultWidth = 800;
  private static final int defaultHeight = 500;
  private static Stage stage;

  /**
   * method to set our scene
   * @param fxml - path to fxml
   * @param title - title of our new scene
   * @param width - width of our new scene
   * @param height - height of our scene
   * @throws IOException - thrown when we can't get the xml file
   * @return
   */
  public Parent setScene(String fxml, String title, int width, int height) throws IOException {

    int theWidth = width != 0 ? width : defaultWidth;
    int theHeight = height != 0 ? height : defaultHeight;

    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    try {
      Parent scene = FXMLLoader.load(getClass().getResource(fxml), bundle);
      stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(scene, theWidth, theHeight));
      stage.show();
      return scene;
    } catch(Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  /**
   * method to get the current stage
   * @return
   */
  public static final Stage getCurrentStage() {
    return stage;
  }
  /**
   * Method to show a standard alert to the user
   * @param alertType - the type of alert we want to show
   * @param owner - the window owner
   * @param title - title of our alert
   * @param message - actual message
   */
  public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
    String lang = System.getProperty("user.language");
    Locale locale = new Locale(lang, lang.toUpperCase());
    ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(owner);
    alert.show();
  }
}
