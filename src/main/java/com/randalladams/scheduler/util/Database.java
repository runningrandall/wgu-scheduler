package com.randalladams.scheduler.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class Database {

  private static String DATABASE_HOST;
  private static String DATABASE_USERNAME;
  private static String DATABASE_PASSWORD;

  public Database() {
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      File configFile = new File(Objects.requireNonNull(classloader.getResource("application.properties")).getFile());
      FileReader reader = new FileReader(configFile);
      Properties props = new Properties();
      props.load(reader);

      DATABASE_HOST = props.getProperty("db.host");
      DATABASE_USERNAME = props.getProperty("db.username");
      DATABASE_PASSWORD = props.getProperty("db.password");

      reader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Could not get configuration file" + ex.getMessage());
    } catch (IOException ex) {
      System.out.println("Could not get file" + ex.getMessage());
    }
  }

  public Connection getConnection() throws SQLException {
    return DriverManager
      .getConnection(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD);
  }

  public static void printSQLException(SQLException ex) {
    for (Throwable e : ex) {
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
}