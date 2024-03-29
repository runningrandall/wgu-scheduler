package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Login;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.LogUtil;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.ZoneId;

/**
 * Service class for handling logins
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class LoginService {

  private static Connection conn;
  private static final String DATABASE_TABLE = "client_schedule.users";
  private static final String LOG_FILE = "login_activity.txt";

  /**
   * constructor to create connection to the database
   */
  public LoginService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  /**
   * method to see if a user has a valid login
   * @param username - user's username
   * @param password - user's password
   * @return validLogin - boolean
   * @throws SQLException - exception for when sql can't be executed
   * @throws NoSuchAlgorithmException - exception for when it can't encrypt the password
   */
  public boolean isValidLogin(String username, String password) throws SQLException, NoSuchAlgorithmException {
    String loginQuery = "SELECT * FROM " + DATABASE_TABLE + " WHERE User_name = ? AND Password = ? LIMIT 1";
    PreparedStatement preparedStatement = conn.prepareStatement(loginQuery);
    preparedStatement.setString(1, username);
    // TODO: remove encryption...not sure why this class allows unencrypted passwords AT ALL
    preparedStatement.setString(2, encryptPassword(password));
    // Execute the query
    ResultSet resultSet = preparedStatement.executeQuery();
    boolean isValid = false;
    while (resultSet.next()) {
      UserSession.getInstance(resultSet.getString("User_Name"), resultSet.getInt("User_ID"));
      isValid = true;
    }
    logLoginMessage(username, isValid);
    return isValid;
  }

  /**
   * function to get the current timezone id
   * @return String - timezone as string
   */
  public String getTimezoneId() {
    ZoneId zone = ZoneId.systemDefault();
    return zone.toString();
  }

  /**
   * helper method to encrypt the password
   * @param password string
   * @return string
   * @throws NoSuchAlgorithmException
   */
  public static String encryptPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());

    byte[] byteData = md.digest();

    StringBuilder sb = new StringBuilder();
    for (byte byteDatum : byteData) sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
    return sb.toString();
  }

  /**
   * method to log the login attempt to a file
   * @param username string
   * @param isSuccessful bool
   */
  private void logLoginMessage(String username, boolean isSuccessful) {
    LogUtil logUtil = new LogUtil(LOG_FILE);
    String successString = isSuccessful ? "Successful" : "Failed";
    logUtil.logInfo("username=" + username + " action=Login status=" + successString);
  }

  /**
   * method to parse the login activity log and return in a list
   * @return ObservableList of Logins
   * @throws IOException
   */
  public ObservableList<Login> getLoginReport() throws IOException {
    ObservableList<Login> logins = FXCollections.observableArrayList();
    BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE));
    String loginEntry = reader.readLine();
    while(loginEntry != null) {
      if (loginEntry.startsWith("INFO:")) {
        String[] splitByColon = loginEntry.split(":");
        String dateString = (splitByColon[1] + ":" + splitByColon[2] + ":" + splitByColon[3]).trim();

        String[] splitBySpace = splitByColon[4].trim().split(" ");
        String username = splitBySpace[0].split("=")[1];
        String status = splitBySpace[2].split("=")[1];
        logins.add(new Login(username, dateString, status));
      }
      loginEntry = reader.readLine();
    }
    return logins;
  }
}