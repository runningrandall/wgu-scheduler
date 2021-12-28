package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.ReportAppointmentType;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.UserSession;
import com.randalladams.scheduler.util.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Service class for managing and gettign appointments
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class AppointmentService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "appointments";
  private static final int VALID_START_HOUR_ET = 7; // 8am eastern
  private static final int VALID_END_HOUR_ET = 19; // 10pm eastern
  private static final int APPOINTMENT_START_ALERT_IN_MINUTES = 15;
  private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String FILTER_WEEK = "WEEK";
  private static final String FILTER_MONTH = "MONTH";
  private Database db;

  /**
   * default constructor
   * always connects to the db
   */
  public AppointmentService() {
    try {
      db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

  /**
   * gets an appointment by the appointment id
   * @param appointmentId int
   * @return Appointment
   * @throws SQLException
   */
  public static Appointment getAppointmentById(int appointmentId) throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " a LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID WHERE a.Appointment_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
    preparedStatement.setInt(1, appointmentId);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return new Appointment(
      resultSet.getInt("a.Appointment_ID"),
      resultSet.getString("a.Title"),
      resultSet.getString("a.Description"),
      resultSet.getString("a.Location"),
      resultSet.getString("a.Type"),
      resultSet.getString("c.Contact_Name"),
      resultSet.getDate("a.Start"),
      resultSet.getTimestamp("a.Start"),
      resultSet.getDate("a.End"),
      resultSet.getTimestamp("a.End"),
      resultSet.getDate("a.Create_Date"),
      resultSet.getString("a.Created_By"),
      resultSet.getDate("a.Last_Update"),
      resultSet.getString("a.Last_Updated_By"),
      resultSet.getInt("a.Customer_ID"),
      resultSet.getInt("a.User_ID"),
      resultSet.getInt("a.Contact_ID")
    );
  }

  /**
   * deletes all the appointments for a given customer id
   * @param customerId int
   * @throws SQLException
   */
  public void deleteAllAppointmentsByCustomerId (int customerId) throws SQLException {
    String deleteQuery = "DELETE FROM " + DATABASE_TABLE + " WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
    preparedStatement.setInt(1, customerId);
    preparedStatement.executeUpdate();
  }

  /**
   * gets all appointments for a given user id
   * @param userId int
   * @return ObservableList of Appointments
   * @throws SQLException
   */
  public ObservableList<Appointment> getAppointmentsByUserId(int userId) throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " a LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID WHERE a.User_ID = ? ORDER BY Start DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
    preparedStatement.setInt(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    while (resultSet.next()) {
      Appointment appointment = new Appointment(
        resultSet.getInt("a.Appointment_ID"),
        resultSet.getString("a.Title"),
        resultSet.getString("a.Description"),
        resultSet.getString("a.Location"),
        resultSet.getString("a.Type"),
        resultSet.getString("c.Contact_Name"),
        resultSet.getDate("a.Start"),
        resultSet.getTimestamp("a.Start"),
        resultSet.getDate("a.End"),
        resultSet.getTimestamp("a.End"),
        resultSet.getDate("a.Create_Date"),
        resultSet.getString("a.Created_By"),
        resultSet.getDate("a.Last_Update"),
        resultSet.getString("a.Last_Updated_By"),
        resultSet.getInt("a.Customer_ID"),
        resultSet.getInt("a.User_ID"),
        resultSet.getInt("a.Contact_ID")
      );
      appointmentList.add(appointment);
    }
    return appointmentList;
  }

  /**
   * gets all appointments based on a week month
   * @param userId string
   * @param weekOrMonth string representationof a week month
   * @return ObservableList of Appointments
   * @throws SQLException
   */
  public ObservableList<Appointment> getAppointmentsByWeekOrMonth(int userId, String weekOrMonth) throws SQLException {
    LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    LocalDateTime oneMinuteBeforeMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    String startDate;
    if (weekOrMonth.equals(FILTER_WEEK)) {
      startDate = midnight
        .with(DayOfWeek.SUNDAY)
        .minusDays(7)
        .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    }
    else {
      startDate = midnight
        .withDayOfMonth(1)
        .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    }

    String endDate;
    if (weekOrMonth.equals(FILTER_WEEK)) {
      endDate = oneMinuteBeforeMidnight
        .with(DayOfWeek.SATURDAY)
        .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    }
    else {
      endDate = oneMinuteBeforeMidnight
        .plusMonths(1)
        .withDayOfMonth(1)
        .minusDays(1)
        .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    }

    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " " +
      "a LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID " +
      "WHERE a.User_ID = ? AND a.Start BETWEEN ? AND ? ORDER BY Start DESC";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

    preparedStatement.setInt(1, userId);
    preparedStatement.setString(2, startDate);
    preparedStatement.setString(3, endDate);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      Appointment appointment = new Appointment(
        resultSet.getInt("a.Appointment_ID"),
        resultSet.getString("a.Title"),
        resultSet.getString("a.Description"),
        resultSet.getString("a.Location"),
        resultSet.getString("a.Type"),
        resultSet.getString("c.Contact_Name"),
        resultSet.getDate("a.Start"),
        resultSet.getTimestamp("a.Start"),
        resultSet.getDate("a.End"),
        resultSet.getTimestamp("a.End"),
        resultSet.getDate("a.Create_Date"),
        resultSet.getString("a.Created_By"),
        resultSet.getDate("a.Last_Update"),
        resultSet.getString("a.Last_Updated_By"),
        resultSet.getInt("a.Customer_ID"),
        resultSet.getInt("a.User_ID"),
        resultSet.getInt("a.Contact_ID")
      );
      appointmentList.add(appointment);
    }
    return appointmentList;
  }

  /**
   * gets appointments organized by type and month
   * @return ObservableList of Appointments
   * @throws SQLException
   */
  public ObservableList<ReportAppointmentType> getAppointmentsByTypeAndMonth() throws SQLException {

    String selectQuery = "SELECT EXTRACT( YEAR_MONTH FROM `Start` ) as `Year_Month`, Type, COUNT(*) as `Total` FROM " + DATABASE_TABLE + " GROUP BY Type";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<ReportAppointmentType> resultList = FXCollections.observableArrayList();

    while (resultSet.next()) {
      ReportAppointmentType rat = new ReportAppointmentType(
        resultSet.getString("Type"),
        resultSet.getString("Year_Month"),
        resultSet.getInt("Total")
      );
      resultList.add(rat);
    }
    return resultList;
  }

  /**
   * gets appointments and organizes them by contact name
   * @return ObservableList of Appointments
   * @throws SQLException
   */
  public ObservableList<Appointment> getContactsAppointmentSchedule() throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE +" a " +
      "LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID " +
      "WHERE start >= curdate() " +
      "ORDER BY c.Contact_Name, Start";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    while (resultSet.next()) {
      Appointment appointment = new Appointment(
        resultSet.getInt("a.Appointment_ID"),
        resultSet.getString("a.Title"),
        resultSet.getString("a.Description"),
        resultSet.getString("a.Location"),
        resultSet.getString("a.Type"),
        resultSet.getString("c.Contact_Name"),
        resultSet.getDate("a.Start"),
        resultSet.getTimestamp("a.Start"),
        resultSet.getDate("a.End"),
        resultSet.getTimestamp("a.End"),
        resultSet.getDate("a.Create_Date"),
        resultSet.getString("a.Created_By"),
        resultSet.getDate("a.Last_Update"),
        resultSet.getString("a.Last_Updated_By"),
        resultSet.getInt("a.Customer_ID"),
        resultSet.getInt("a.User_ID"),
        resultSet.getInt("a.Contact_ID")
      );
      appointmentList.add(appointment);
    }
    return appointmentList;
  }

  /**
   * gets any appointment occuring within 15 minutes of login
   * @param userId int
   * @return ObservableList of Appointments
   * @throws SQLException
   */
  public Appointment getAppointmentWithinFifteenMinutes(int userId) throws SQLException {

    String currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
      .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    String dateTimePlusFifteen = LocalDateTime.now(ZoneOffset.UTC)
      .plusMinutes(APPOINTMENT_START_ALERT_IN_MINUTES)
      .format(DateTimeFormatter.ofPattern(DB_DATE_FORMAT));
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " " +
      "a LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID " +
      "WHERE a.User_ID = ? AND a.Start BETWEEN ? AND ? LIMIT 1";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

    preparedStatement.setInt(1, userId);
    preparedStatement.setString(2, currentDateTime);
    preparedStatement.setString(3, dateTimePlusFifteen);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      return new Appointment(
        resultSet.getInt("a.Appointment_ID"),
        resultSet.getString("a.Title"),
        resultSet.getString("a.Description"),
        resultSet.getString("a.Location"),
        resultSet.getString("a.Type"),
        resultSet.getString("c.Contact_Name"),
        resultSet.getDate("a.Start"),
        resultSet.getTimestamp("a.Start"),
        resultSet.getDate("a.End"),
        resultSet.getTimestamp("a.End"),
        resultSet.getDate("a.Create_Date"),
        resultSet.getString("a.Created_By"),
        resultSet.getDate("a.Last_Update"),
        resultSet.getString("a.Last_Updated_By"),
        resultSet.getInt("a.Customer_ID"),
        resultSet.getInt("a.User_ID"),
        resultSet.getInt("a.Contact_ID")
      );
    }
    return null;
  }

  /**
   * checks to see if the appointment time is valid
   * no overlapping or outside business hours
   * @param appointmentDate LocalDateTime
   * @return
   */
  private Boolean isValidAppointmentTime(LocalDateTime appointmentDate) {
    ZoneId easternZoneId = ZoneId.of("America/New_York");
    int appointmentHour = appointmentDate.atZone(easternZoneId).getHour();
    int appointmentMinute = appointmentDate.atZone(easternZoneId).getMinute();
    Boolean isValidHour = appointmentHour >= VALID_START_HOUR_ET && appointmentHour <= VALID_END_HOUR_ET;
    Boolean isValidMinute = appointmentHour != VALID_END_HOUR_ET || appointmentMinute == 0;
    return isValidHour && isValidMinute;
  }

  /**
   * verifies overlapping appointment dates for a given contact id
   * @param customerId int
   * @param start LocalDateTime
   * @param end LocalDateTime
   * @return bool
   * @throws SQLException
   */
  private Boolean customerHasOverlappingAppointment(int customerId, LocalDateTime start, LocalDateTime end) throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " " +
      "WHERE ((Start BETWEEN ? AND ?) OR (End BETWEEN ? AND ?)) AND Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

    preparedStatement.setString(1, db.getDbStringFromLocalDateTime(start));
    preparedStatement.setString(2, db.getDbStringFromLocalDateTime(end));
    preparedStatement.setString(3, db.getDbStringFromLocalDateTime(start));
    preparedStatement.setString(4, db.getDbStringFromLocalDateTime(end));
    preparedStatement.setInt(5, customerId);

    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      return true;
    }
    return false;
  }

  /**
   * validator for an appointment before update/create
   * @param isNew boolean
   * @param title string
   * @param description string
   * @param location string
   * @param type string
   * @param start date
   * @param end date
   * @param customerId int
   * @param userId int
   * @param contactId int
   * @return Appointment
   * @throws SQLException
   */
  public Validator validateAppointment(Boolean isNew, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
    boolean isAnyEmpty = title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() || start == null || end == null || customerId == 0 || userId == 0 || contactId == 0;

    // TODO: i18n
    if (isAnyEmpty) {
      return new Validator(false, "Missing one or more required fields");
    }

    if (start.isAfter(end)) {
      return new Validator(false, "Invalid start/end times. Start time cannot be after end time");
    }

    if(!this.isValidAppointmentTime(start)) {
      return new Validator(false, "The start time must be between 8am and 10pm ET");
    }

    if (!this.isValidAppointmentTime(end)) {
      return new Validator(false, "The end time must be between 8am and 10pm ET");
    }

    if (isNew) {
      if (this.customerHasOverlappingAppointment(customerId, start, end)) {
        return new Validator(false, "The customer already has an appointment during this time.");
      }
    }

    return new Validator(true, "");
  }

  /**
   * create method for an appointment
   * @param title string
   * @param description string
   * @param location string
   * @param type string
   * @param start string
   * @param end string
   * @param customerId int
   * @param userId int
   * @param contactId int
   * @return Appointment
   * @throws SQLException
   */
  public Appointment createAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
    String insertQuery = "INSERT INTO `client_schedule`.`appointments`\n" +
      "(`Title`,\n" +
      "`Description`,\n" +
      "`Location`,\n" +
      "`Type`,\n" +
      "`Start`,\n" +
      "`End`,\n" +
      "`Create_Date`,\n" +
      "`Created_By`,\n" +
      "`Last_Update`,\n" +
      "`Last_Updated_By`,\n" +
      "`Customer_ID`,\n" +
      "`User_ID`,\n" +
      "`Contact_ID`)\n" +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
    Date currentDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
    java.sql.Date createDate = sqlDate;
    java.sql.Date lateUpdateDate = sqlDate;
    String createUserName = UserSession.getUserName();

    preparedStatement.setString(1, title);
    preparedStatement.setString(2, description);
    preparedStatement.setString(3, location);
    preparedStatement.setString(4, type);
    preparedStatement.setString(5, db.getDbStringFromLocalDateTime(start));
    preparedStatement.setString(6, db.getDbStringFromLocalDateTime(end));
    preparedStatement.setDate(7, createDate);
    preparedStatement.setString(8, createUserName);
    preparedStatement.setDate(9, lateUpdateDate);
    preparedStatement.setString(10, createUserName);
    preparedStatement.setInt(11, customerId);
    preparedStatement.setInt(12, userId);
    preparedStatement.setInt(13, contactId);

    int affectedRows = preparedStatement.executeUpdate();

    if (affectedRows == 0) {
      throw new SQLException("Creating appointment failed");
    }

    Appointment newAppointment;
    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        int newAppointmentId = generatedKeys.getInt(1);
        newAppointment = getAppointmentById(newAppointmentId);
      }
      else {
        throw new SQLException("Creating customer failed, missing id");
      }
    }

    return newAppointment;
  }

  /**
   * updates an appointment with a given appointment id
   * @param appointmentId int
   * @param title string
   * @param description string
   * @param location string
   * @param type string
   * @param start string
   * @param end string
   * @param customerId int
   * @param userId int
   * @param contactId int
   * @return Appointment
   * @throws SQLException
   */
  public Appointment updateAppointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
    String updateQuery = "UPDATE `client_schedule`.`appointments`\n" +
      "SET\n" +
      "`Title` = ?,\n" +
      "`Description` = ?,\n" +
      "`Location` = ?,\n" +
      "`Type` = ?,\n" +
      "`Start` = ?,\n" +
      "`End` = ?,\n" +
      "`Last_Update` = ?,\n" +
      "`Last_Updated_By` = ?,\n" +
      "`Customer_ID` = ?,\n" +
      "`User_ID` = ?,\n" +
      "`Contact_ID` = ?\n" +
      "WHERE `Appointment_ID` = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
    Date currentDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

    preparedStatement.setString(1, title);
    preparedStatement.setString(2, description);
    preparedStatement.setString(3, location);
    preparedStatement.setString(4, type);
    preparedStatement.setString(5, db.getDbStringFromLocalDateTime(start));
    preparedStatement.setString(6, db.getDbStringFromLocalDateTime(end));
    preparedStatement.setDate(7, sqlDate);
    preparedStatement.setString(8, UserSession.getUserName());
    preparedStatement.setInt(9, customerId);
    preparedStatement.setInt(10, userId);
    preparedStatement.setInt(11, contactId);
    preparedStatement.setInt(12, appointmentId);

    preparedStatement.executeUpdate();

    return getAppointmentById(appointmentId);
  }

  /**
   * deletes an appointment with a given appointment id
   * @param appointmentId int
   * @throws SQLException
   */
  public static void deleteAppointmentById(int appointmentId) throws SQLException {
    String deleteQuery = "DELETE FROM " + DATABASE_TABLE + " WHERE Appointment_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
    preparedStatement.setInt(1, appointmentId);
    preparedStatement.executeUpdate();
  }
}
