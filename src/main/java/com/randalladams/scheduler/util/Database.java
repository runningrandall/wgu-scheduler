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
    return getUtcDateTimeFromLocalDateTime(date).format(formatter);
  }

  /**
   * function to convert an local timezone date to a utc date
   * @param localDt the local datetime
   * @return LocalDateTie
   */
  public static LocalDateTime getUtcDateTimeFromLocalDateTime(LocalDateTime localDt) {
    return localDt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
  }

  /**
   * method to get a timestamp in local time from a utc timestamp
   * @param dbDate timestamp
   * @return Timestamp
   */
  public static LocalDateTime getZonedDateTimeFromDbDate(LocalDateTime dbDate) {
    return dbDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * method to get datetime in utc from localdate time
   * @return Timestamp
   */
  public static LocalDateTime getUtcDateFromLocalDateTime() {
    return LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
  }

  /**
   * converts a local date time to a proper converted string
   * @param dateTimeToConvert the local date time to convert
   * @return String
   */
  public static String getDateDisplayString(LocalDateTime dateTimeToConvert) {
    return dateTimeToConvert.format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
  }

  /**
   * converts a localdatetime in users current timezone and converts it to eastern
   * @return ZonedDateTime
   */
  public static ZonedDateTime getCurrentDbTimeInEst() {
    ZoneId z = ZoneId.of(EST_TIMEZONE);
    return LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(z);
  }

  /**
   * converts a localdatetime in users current timezone
   * @return ZonedDateTime
   */
  public static ZonedDateTime getCurrentDbTimeInLocal() {
    ZoneId z = ZoneId.systemDefault();
    return LocalDateTime.now().atZone(z);
  }

  /**
   * converts a local date time and converts it to eastern
   * @param localDate the local date time
   * @return ZonedDateTime
   */
  public static ZonedDateTime getEstFromZoneLocalDateTime(LocalDateTime localDate) {
    return localDate.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(EST_TIMEZONE));
  }

  /**
   * converts a local date time and converts it to eastern
   * @param localDate the local date time
   * @return ZonedDateTime
   */
  public static ZonedDateTime getLocalZonedDateTimeFromLocalDateTime(LocalDateTime localDate) {
    return localDate.atZone(ZoneId.systemDefault());
  }
}