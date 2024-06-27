package com.example.barberapp;

import com.example.barberapp.database.DataBaseConnection;

import java.sql.*;
import java.util.Date;

public class BarberManager {

    private final String babername;
    private final int userId;

    // constructor of the class
    public BarberManager(String babername, int userId){
        this.babername = babername;
        this.userId = userId;
    }

    // Method to create a new barber
    public boolean createBarber() {
        // Implementation for creating a new barber
        String checkUserSql = "SELECT COUNT(*) FROM barbers WHERE barbername = ?";   // SQL to check if the username exists
        String insertUserSql = "INSERT INTO barbers(user_id, barbername) VALUES(?,?)"; // SQL to insert a new user

        try (Connection conn = DataBaseConnection.connect();                          // Get connection to the database
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql);       // Initialize statement to check username
             PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {   // Initialize statement to insert user

            checkStmt.setString(1, this.babername);                                    // Set the username parameter for checking
            ResultSet rs = checkStmt.executeQuery();                                  // Execute the query to check if the username exists

            if (rs.next() && rs.getInt(1) > 0) {                                    // If the username exists
                return true;                                                              // Return true indicating the user already exists
            }

            // If username does not exist, insert the new user
            insertStmt.setInt(1, this.userId);                                   // Set the new username
            insertStmt.setString(2, this.babername);                                   // Set the new password
            insertStmt.executeUpdate();                                               // Update the users table

        } catch (SQLException e) {
            System.out.println(e.getMessage());                                       // Handle any SQL exceptions (optional: print the exception message for debugging)
        }
        return false;                                                                 // Return false indicating the user was successfully registered
    }


    // Method to remove an existing barber
    public boolean removeBarber() {
        // SQL query to delete a user
        String sql = "DELETE FROM barbers WHERE barbername = ?";

        try (Connection conn = DataBaseConnection.connect();           // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // Prepare the SQL statement

            pstmt.setString(1, this.babername);       // Set the username parameter in the prepared statement
            pstmt.executeUpdate();              // Execute the update to delete the user

            return true;
            //System.out.println("User '" + username + "' has been deleted.");  // Print a message indicating successful deletion
        } catch (SQLException e) {
            //System.out.println("Error deleting user '" + username + "': " + e.getMessage());  // Handle any SQL exceptions (print error message for debugging)
            return false;
        }
    }

    // Method to modify an existing barber name
    public boolean modifyBarber(String newBarberName){

        // SQL to update the password for the given username
        String modifyBarberSql = "UPDATE barbers SET barbername = ? WHERE barbername = ?";

        // Try-with-resources to ensure the database connection and statement are closed automatically
        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(modifyBarberSql)) { // Prepare the SQL statement

            pstmt.setString(1, newBarberName); // Set the new password parameter in the prepared statement
            pstmt.setString(2, this.babername); // Set the new password parameter in the prepared statement

            int rowsAffected = pstmt.executeUpdate(); // Execute the update query and get the number of rows affected

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Method to get barberID
    public int getBarberId() {
        String sql = "SELECT id FROM barbers WHERE barbername = ?"; // Query to get the user ID for the given username

        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            pstmt.setString(1, this.babername); // Set the username parameter
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                //System.out.println("User ID found: " + userId);
                return rs.getInt("id"); // Return the user ID
            } else {
                //System.out.println("Username not found.");
                return -1; // Return -1 if the username is not found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Print the SQL exception message
            return -1; // Return -1 in case of an SQL exception
        }
    }
}
