package com.randalladams.login;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class LoginController {

  @FXML
  private TextField username;

  @FXML
  private PasswordField password;

  @FXML
  private Button loginButton;

  @FXML
  public void login(ActionEvent event) throws SQLException {

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

    LoginDao loginDao = new LoginDao();
    boolean isValidLogin =  loginDao.isValidLogin(usernameText, passwordText);

    if(isValidLogin) {
      showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful!",
        "Welcome " + usernameText);
    } else {
      showAlert(Alert.AlertType.ERROR, owner, "Login Failed!",
        "Incorrect credentials for username:  " + usernameText);
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