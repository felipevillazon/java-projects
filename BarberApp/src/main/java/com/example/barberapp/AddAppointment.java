package com.example.barberapp;

import com.example.barberapp.database.DataBaseConnection;

import java.sql.*;

public class AddAppointment {

    private final int userId;                // internal variable "username" of the class
    private final int barberId;

    // constructor of the class
    public AddAppointment(int userId, int barberId){
        this.userId = userId;
        this.barberId = barberId;
    }

    /* main method of the class. it adds the values for the Date and time selected by the user to the database*/
    public boolean addAppointment(Date appointmentDate, Time appointmentTime) {

        // SQL to check if the date and time already exist for the user and barber
        String checkAppointmentSql = "SELECT COUNT(*) FROM appointments WHERE user_id = ? AND barber_id = ? AND date = ? AND time = ? AND is_taken = 1";

        // SQL to insert a new appointment
        String insertAppointmentSql = "INSERT INTO appointments(user_id, barber_id, date, time, is_taken) VALUES(?, ?, ?, ?, 1)";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkAppointmentSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertAppointmentSql)) {

            // Check if the date and time already exist
            checkStmt.setInt(1, this.userId);
            checkStmt.setInt(2, this.barberId);
            checkStmt.setDate(3, appointmentDate);
            checkStmt.setTime(4, appointmentTime);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Date and time already exist
                return false;
            } else {
                // If date and time do not exist, insert the new appointment
                insertStmt.setInt(1, this.userId);
                insertStmt.setInt(2, this.barberId);
                insertStmt.setDate(3, appointmentDate);
                insertStmt.setTime(4, appointmentTime);


                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }


    /* method to get the appointment id. it helps because sometimes you want to cancel an appointment,
    * and you need to perform action on a given row of the database*/
    public int getAppointmentId(Date date, Time time) {
        String sql = "SELECT appointment_id FROM appointments WHERE user_id = ? AND barber_id = ? AND date = ? AND time = ? AND is_taken = 1"; // Query to get the user ID for the given username

        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            pstmt.setInt(1, this.userId); // Set the username parameter
            pstmt.setInt(2, this.barberId); // Set the username parameter
            pstmt.setDate(3, date); // Set the username parameter
            pstmt.setTime(4, time); // Set the username parameter
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                //System.out.println("User ID found: " + userId);
                return rs.getInt("appointment_id"); // Return the user ID
            } else {
                //System.out.println("Username not found.");
                return -1; // Return -1 if the username is not found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Print the SQL exception message
            return -1; // Return -1 in case of an SQL exception
        }
    }

    /* separate method that add independently the client name to the database*/
    public boolean addClientName(Date appointmentDate, Time appointmentTime, String clientName) {

        // SQL to update an existing appointment with clientName
        String updateAppointmentSql = "UPDATE appointments SET clientName = ? WHERE user_id = ? AND barber_id = ? AND date = ? AND time = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement updateStmt = conn.prepareStatement(updateAppointmentSql)) {

            // Set the parameters for the prepared statement
            updateStmt.setString(1, clientName);
            updateStmt.setInt(2, this.userId);
            updateStmt.setInt(3, this.barberId);
            updateStmt.setDate(4, appointmentDate);
            updateStmt.setTime(5, appointmentTime);

            // Execute the update
            int rowsAffected = updateStmt.executeUpdate();

            // Check if the update was successful
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error adding client name: " + e.getMessage());
            return false;
        }
    }

    /* method to get the client name for a given time slot. it is used for example to initialize the daily calendar*/
    public String getClientName(int appointmentId){

        String sql = "SELECT clientName FROM appointments WHERE user_id = ? AND barber_id = ? AND appointment_id = ?"; // Query to get the use

        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            pstmt.setInt(1, this.userId); // Set the username parameter
            pstmt.setInt(2, this.barberId); // Set the username parameter
            pstmt.setInt(3, appointmentId); // Set the username parameter
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                //System.out.println("User ID found: " + userId);
                return rs.getString("clientName"); // Return the user ID
            } else {
                //System.out.println("Username not found.");
                return null; // Return -1 if the username is not found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Print the SQL exception message
            return null; // Return -1 in case of an SQL exception
        }



    }


}

