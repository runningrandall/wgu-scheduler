package com.randalladams.scheduler.login;
/*
 * LoginService is service for processing the login scene
 * @author Randall Adams
 * @version 1.0.0
 * @since 06/01/2020
 */

import com.randalladams.scheduler.util.Database;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.ZoneId;

public class LoginService {

  private static Connection conn;
  private static final String DATABASE_TABLE = "users";

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
    String LOGIN_QUERY = "SELECT * FROM " + DATABASE_TABLE + " WHERE user_name = ? AND Password = ? LIMIT 1";
      PreparedStatement preparedStatement = conn.prepareStatement(LOGIN_QUERY);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, encryptPassword(password));

      // Execute the query
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
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
   * function to encrypt the password text
   * @param password - the user's password
   * @return string - the md5 hashed password
   * @throws NoSuchAlgorithmException - thrown when we are missing md5
   */
  public static String encryptPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());

    byte[] byteData = md.digest();

    StringBuilder sb = new StringBuilder();
    for (byte byteDatum : byteData) sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
    return sb.toString();
  }
}