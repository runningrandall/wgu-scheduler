package com.randalladams.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOConnection {
  final static Logger log = Logger.getLogger(DAOConnection.class.getName());

  private static String driver = config.prop.getProperty("db.driver");

  private static String url = config.prop.getProperty("db.url");

  private static String user = config.prop.getProperty("db.user");

  private static String password = config.prop.getProperty("db.password");

  private static Connection connection;
  /**
   * Private constructor so this class cannot be instantiated only by it self.
   */
  private DAOConnection()
  {
  }

  /**
   * Create and return the Connection if not exist.
   *
   * @return The connection object
   */
  public static Connection getInstance()
  {
    if (connection == null)
    {
      try
      {
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        log.log(Level.SEVERE,  "DB Driver error : " + e);
      }

      try
      {
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e)  {
        log.log(Level.SEVERE, "DB Connection error : " + e);
      }
    }

    return connection;
  }

}
