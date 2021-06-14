package com.randalladams.login;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

// TODO: extend dao class
public class LoginDao {

  private static String DATABASE_HOST;
  private static String DATABASE_USERNAME;
  private static String DATABASE_PASSWORD;
  private static final String DATABASE_TABLE = "users";
  private static String LOGIN_QUERY;

  public LoginDao() {
    // TODO: move this to a extend of sorts (super class dao)
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      File configFile = new File(Objects.requireNonNull(classloader.getResource("application.properties")).getFile());
      FileReader reader = new FileReader(configFile);
      Properties props = new Properties();
      props.load(reader);

      DATABASE_HOST = props.getProperty("db.host");
      DATABASE_USERNAME = props.getProperty("db.username");
      DATABASE_PASSWORD = props.getProperty("db.password");
      String DATABASE_SCHEMA = props.getProperty("db.schema");
      LOGIN_QUERY = "SELECT * FROM " + DATABASE_SCHEMA + "." + DATABASE_TABLE + " WHERE user_name = ? AND Password = ? LIMIT 1";
      reader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Could not login" + ex.getMessage());
    } catch (IOException ex) {
      System.out.println("Could not get file" + ex.getMessage());
    }
  }

  public boolean isValidLogin(String username, String password) throws SQLException {

    try (Connection connection = DriverManager
      .getConnection(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD);

         // Step 2:Create a statement using connection object
         PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_QUERY)) {
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, encryptPassword(password));

      // Step 3: Execute the query or update query
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    } catch (SQLException | NoSuchAlgorithmException e) {
      // print SQL exception information
      assert e instanceof SQLException;
      printSQLException((SQLException) e);
      throw new SQLException("Unable to determine if the user is logged in");
    }
  }

  public static void printSQLException(SQLException ex) {
    for (Throwable e: ex) {
      if (e instanceof SQLException) {
        e.printStackTrace(System.err);
        System.err.println("SQLState: " + ((SQLException) e).getSQLState());
        System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
        System.err.println("Message: " + e.getMessage());
        Throwable t = ex.getCause();
        while (t != null) {
          System.out.println("Cause: " + t);
          t = t.getCause();
        }
      }
    }
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