package com.randalladams.scheduler.login;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController implements Initializable {

  private static LoginService loginService;

  @FXML
  private TextField username;

  @FXML
  private PasswordField password;

  @FXML
  private Button loginButton;

  @FXML
  private Label timezoneLabel;

  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      loginService = new LoginService();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    String timezoneId = loginService.getTimezoneId();
    timezoneLabel.setText(timezoneId);
  }

  @FXML
  public void login() {

    Window owner = loginButton.getScene().getWindow();


    if (username.getText().isEmpty()) {
      showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
        "Please enter your username");
      return;
    }

    if (password.getText().isEmpty()) {
      showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
        "Please enter your password");
      return;
    }

    String usernameText = username.getText();
    String passwordText = password.getText();


    try {
      boolean isValidLogin = loginService.isValidLogin(usernameText, passwordText);

      if (isValidLogin) {
        // TODO...make parent class and pass the appriopriate behaviors
        String lang = System.getProperty("user.language");
        Locale locale = new Locale(lang, lang.toUpperCase());
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/contacts.fxml"));
        Parent scene = fxmlLoader.load(getClass().getResource("/fxml/contacts.fxml"), bundle);
        Stage stage = new Stage();
        stage.setTitle("Contacts");
        stage.setScene(new Scene(scene, 800, 500));
        stage.show();
        owner.hide();
      } else {
        showAlert(Alert.AlertType.ERROR, owner, "Login Failed!",
          "Incorrect credentials for username:  " + usernameText);
      }
    } catch (NoSuchAlgorithmException | SQLException | IOException e) {
      System.out.println("Error trying to login: " + e.getMessage());
    }
  }

  private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(owner);
    alert.show();
  }
}