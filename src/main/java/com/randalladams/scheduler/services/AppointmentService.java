package com.randalladams.scheduler.services;

import com.randalladams.scheduler.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
