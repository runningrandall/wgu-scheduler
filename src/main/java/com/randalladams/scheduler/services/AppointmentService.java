package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
