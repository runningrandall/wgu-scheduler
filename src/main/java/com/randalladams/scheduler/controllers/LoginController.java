package com.randalladams.scheduler.controllers;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.randalladams.scheduler.services.LoginService;
import com.randalladams.scheduler.util.Lang;
import com.randalladams.scheduler.util.SceneManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Window;

/**
 * Login controller class for handling the login scene
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class LoginController implements Initializable {

  private static LoginService loginService;
  private static final String nextFxmlScene = "/fxml/home.fxml";
  private static final SceneManager sm = new SceneManager();
  private static final String nextSceneTitle = Lang.getString("login.header");

  @FXML
  private TextField username;

  @FXML
  private PasswordField password;

  @FXML
  private Button loginButton;

  @FXML
  private Label timezoneLabel;

  /**
   * Initializer for login scene
   * @param url - the url (required for all initializers)
   * @param resourceBundle - the resource bundle for the scene
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loginService = new LoginService();
    String timezoneId = loginService.getTimezoneId();
    timezoneLabel.setText(timezoneId);
  }

  /**
   * Login method that triggers a service to check for valid login
   * credentials and then load subsequent default scene (contacts
   * shows an alert if unsuccessful
   */
  @FXML
  public void login() throws IOException {

    Window owner = loginButton.getScene().getWindow();
    String formError = Lang.getString("login.form_error");

    if (username.getText().isEmpty()) {
      SceneManager.showAlert(Alert.AlertType.ERROR, owner, formError,
        Lang.getString("login.invalid_username"));
      return;
    }

    if (password.getText().isEmpty()) {
      SceneManager.showAlert(Alert.AlertType.ERROR, owner, formError,
        Lang.getString("login.invalid_password"));
      return;
    }

    String usernameText = username.getText();
    String passwordText = password.getText();
    boolean isValidLogin = false;

    try {
      isValidLogin = loginService.isValidLogin(usernameText, passwordText);
    } catch (NoSuchAlgorithmException | SQLException e) {
      System.out.println("Error trying to login: " + e.getMessage());
    }
      // valid login path
    if (isValidLogin) {
      sm.setScene(nextFxmlScene, nextSceneTitle, 800, 500);
      owner.hide();
    } else { // if login fails we show an error
      SceneManager.showAlert(Alert.AlertType.ERROR, owner, Lang.getString("login.invalid_credentials"),
        Lang.getString("login.invalid_credentials_message") + usernameText);
    }
  }
}