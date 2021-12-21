package com.randalladams.scheduler.services;

import com.randalladams.scheduler.model.Appointment;
import com.randalladams.scheduler.model.Customer;
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

  public void deleteAllAppointmentsByCustomerId (int customerId) throws SQLException {
    String deleteQuery = "DELETE FROM " + DATABASE_TABLE + " WHERE Customer_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
    preparedStatement.setInt(1, customerId);
    preparedStatement.executeUpdate();
  }

  public ObservableList<Appointment> getAppointmentsByUserId(int userId) throws SQLException {
    String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " WHERE User_ID = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
    ResultSet resultSet = preparedStatement.executeQuery();
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    while (resultSet.next()) {
      Appointment appointment = new Appointment(
        resultSet.getInt("Appointment_ID"),
        resultSet.getString("Title"),
        resultSet.getString("Description"),
        resultSet.getString("Location"),
        resultSet.getString("Type"),
        resultSet.getDate("Start"),
        resultSet.getDate("End"),
        resultSet.getDate("Create_Date"),
        resultSet.getString("Created_By"),
        resultSet.getDate("Last_Update"),
        resultSet.getString("Last_Updated_By"),
        resultSet.getInt("Customer_ID"),
        resultSet.getInt("User_ID"),
        resultSet.getInt("Contact_ID")
      );
      appointmentList.add(appointment);
    }
    return appointmentList;
  }
}
