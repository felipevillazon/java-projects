package com.example.barberapp;

import com.example.barberapp.database.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveAppointment {

    private final int userId;                // internal variable "username" of the class
    private final int barberId;
    private final int appointmentId;

    // constructor of the class
    public RemoveAppointment (int userId, int barberId, int appointmentId){
        this.userId = userId;
        this.barberId = barberId;
        this.appointmentId = appointmentId;
    }

    // method to cancel the appointment or better described, to delete the appointment from the database
    public boolean cancelAppointment() {
        // SQL query to delete a user
        String sql = "DELETE FROM appointments WHERE appointment_id= ? AND user_id = ? AND barber_id = ?";

        try (Connection conn = DataBaseConnection.connect();           // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // Prepare the SQL statement

            pstmt.setInt(1, this.appointmentId);       // Set the username parameter in the prepared statement
            pstmt.setInt(2, this.userId);       // Set the username parameter in the prepared statement
            pstmt.setInt(3, this.barberId);       // Set the username parameter in the prepared statement

            pstmt.executeUpdate();              // Execute the update to delete the user

            return true;
            //System.out.println("User '" + username + "' has been deleted.");  // Print a message indicating successful deletion
        } catch (SQLException e) {
            //System.out.println("Error deleting user '" + username + "': " + e.getMessage());  // Handle any SQL exceptions (print error message for debugging)
            return false;
        }
    }



}
