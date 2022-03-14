package com.randalladams.scheduler.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

/**
 * Database class is our class for managing database connections, managing dates, and printing exceptions
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Database {

  private static String DATABASE_HOST;
  private static String DATABASE_USERNAME;
  private static String DATABASE_PASSWORD;
  private static String DATABASE_SCHEMA;
  private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String EST_TIMEZONE = "America/New_York";

  /**
   * Database constructor creates connection and filereader for loading of application properties
   */
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
      DATABASE_SCHEMA = props.getProperty("db.schema");

      reader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Could not get configuration file" + ex.getMessage());
    } catch (IOException ex) {
      System.out.println("Could not get file" + ex.getMessage());
    }
  }

  /**
   * getter for connection
   * @return - DriverManager connection
   * @throws SQLException - exception when unable to connect to the database
   */
  public Connection getConnection() throws SQLException {
    return DriverManager
      .getConnection(DATABASE_HOST + "/" + DATABASE_SCHEMA, DATABASE_USERNAME, DATABASE_PASSWORD);
  }

  /**
   * standard method for printing any sql exceptions
   * @param ex - the sql exception
   */
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

  /**
   * method for getting the db datetime string from a localdatetime
   * @param date ZonedDateTime - local timezone in
   * @return String
   */
  public String getDbDateFromLocalDate(LocalDateTime date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE_FORMAT);
    return getUtcDateTimeFromEstDateTime(date).format(formatter);
  }

  public static LocalDateTime getUtcDateTimeFromEstDateTime(LocalDateTime localDt) {
    return localDt.atZone(ZoneId.of(EST_TIMEZONE)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
  }

  /**
   * method to get a timestamp in local time from a utc timestamp
   * @param dbDate timestamp
   * @return Timestamp
   */
  public static LocalDateTime getZonedDateTimeFromDbDate(LocalDateTime dbDate) {
    return dbDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(EST_TIMEZONE)).toLocalDateTime();
  }

  public static String getDateDisplayString(LocalDateTime dateTimeToConvert) {
    return dateTimeToConvert.format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
  }

  public static ZonedDateTime getCurrentDbTimeInEst() {
    ZoneId z = ZoneId.of(EST_TIMEZONE);
    return LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(z);
  }

  public static ZonedDateTime getEstFromZoneLocalDateTime(LocalDateTime localDate) {
    return localDate.atZone(ZoneId.of(EST_TIMEZONE));
  }
}