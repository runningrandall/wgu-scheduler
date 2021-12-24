package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.util.Database;
import com.randalladams.scheduler.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

public class AppointmentService {
  private static Connection conn;
  private static final String DATABASE_TABLE = "appointments";

  public AppointmentService() {
    try {
      Database db = new Database();
      conn = db.getConnection();
    } catch (SQLException e) {
      Database.printSQLException(e);
    }
  }

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

  public void deleteAllAppointmentsByCustomerId (int customerId) throws SQLException {
    String deleteQuery = "DELETE FROM " + DATABASE_TABLE + " WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
    preparedStatement.setInt(1, customerId);
    preparedStatement.executeUpdate();
  }

  public ObservableList<Appointment> getAppointmentsByUserId(int userId) throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " a LEFT JOIN contacts c ON c.Contact_ID = a.Contact_ID WHERE a.User_ID = ?";
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

  public static Boolean validateAppointment() {
    return true;
  }

  public static Appointment updateAppointment (int appointmentId, String title, String description, String location, String type, Date start, Date end, int customerId, int userId, int contactId) throws SQLException {
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
    preparedStatement.setDate(5, (java.sql.Date) start);
    preparedStatement.setDate(6, (java.sql.Date) end);
    preparedStatement.setDate(7, sqlDate);
    preparedStatement.setString(8, UserSession.getUserName());
    preparedStatement.setInt(9, customerId);
    preparedStatement.setInt(10, userId);
    preparedStatement.setInt(11, contactId);
    preparedStatement.setInt(12, appointmentId);

    preparedStatement.executeUpdate();

    return getAppointmentById(appointmentId);
  }
}
