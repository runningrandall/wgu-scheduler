package com.randalladams.scheduler;
/*
 * MainApp is the initializer/starting point of Javafx app
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

  /**
   * Create our scene manager
   */
  private static final SceneManager sm = new SceneManager();

  /**
   * initialization of the app
   * @param stage - the stage we wish to show (default)
   * @throws Exception - standard exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    sm.setScene("/fxml/login.fxml", "Login", 0, 0);
  }

  public static void main(String[] args) {
    launch(args);
  }
}