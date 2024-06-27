package com.example.barberapp;

import com.example.barberapp.database.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountManager {

    private final String username;                // internal variable "username" of the class
    private final String password;                // internal variable "password" of the class


    // CONSTRUCTOR OF THE CLASS, IT WILL INITIATE THE PRIVATE VARIABLE WITH THE INPUT VALUES
    public UserAccountManager(String username, String password) {
        this.username = username;             // assign the private variable username with the username input
        this.password = password;             // assign the private variable password with the password input
    }

   
    // Method to register a new user
    public boolean register() {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";   // SQL to check if the username exists
        String insertUserSql = "INSERT INTO users(username, password) VALUES(?,?)"; // SQL to insert a new user

        try (Connection conn = DataBaseConnection.connect();                          // Get connection to the database
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql);       // Initialize statement to check username
             PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {   // Initialize statement to insert user

            checkStmt.setString(1, this.username);                                    // Set the username parameter for checking
            ResultSet rs = checkStmt.executeQuery();                                  // Execute the query to check if the username exists

            if (rs.next() && rs.getInt(1) > 0) {                                      // If the username exists
                return false;                                                          // Return true indicating the user already exists
            }

            // If username does not exist, insert the new user
            insertStmt.setString(1, this.username);                                   // Set the new username
            insertStmt.setString(2, this.password);                                   // Set the new password
            insertStmt.executeUpdate();                                               // Update the users table
            return true;


        } catch (SQLException e) {
            System.out.println(e.getMessage());                                       // Handle any SQL exceptions (optional: print the exception message for debugging)
            return false;
        }

                                                                     // Return false indicating the user was successfully registered
    }


    // Method to log in a user
    public boolean login() {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?"; // SQL query to check if username and password match
        // Try-with-resources to ensure the database connection and statement are closed automatically
        try (Connection conn = DataBaseConnection.connect();  // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // Prepare the SQL statement

            pstmt.setString(1, this.username); // Set the username parameter in the prepared statement
            pstmt.setString(2, this.password); // Set the password parameter in the prepared statement

            ResultSet rs = pstmt.executeQuery(); // Execute the query and get the result set

            // Check if a matching record is found
            // If no match is found, return false
            return rs.next();                          // If a match is found, return true
        } catch (SQLException e) {
            System.out.println(e.getMessage());        // Handle any SQL exceptions (optional: you can print the exception message for debugging)
            return false;                              // In case of an exception, return false indicating login failure
        }
    }


    // Method to modify a user's password
    public boolean changePassword(String newPassword) {
        // SQL to update the password for the given username
        String updatePasswordSql = "UPDATE users SET password = ? WHERE username = ?";

        // Try-with-resources to ensure the database connection and statement are closed automatically
        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(updatePasswordSql)) { // Prepare the SQL statement

            pstmt.setString(1, newPassword); // Set the new password parameter in the prepared statement
            pstmt.setString(2, this.username); // Set the username parameter in the prepared statement

            int rowsAffected = pstmt.executeUpdate(); // Execute the update query and get the number of rows affected

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    // Method to delete a user
    public boolean deleteUser() {
        // SQL query to delete a user
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DataBaseConnection.connect();           // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {  // Prepare the SQL statement

            pstmt.setString(1, this.username);       // Set the username parameter in the prepared statement
            pstmt.executeUpdate();              // Execute the update to delete the user

            return true;
            //System.out.println("User '" + username + "' has been deleted.");  // Print a message indicating successful deletion
        } catch (SQLException e) {
            //System.out.println("Error deleting user '" + username + "': " + e.getMessage());  // Handle any SQL exceptions (print error message for debugging)
            return false;
        }
    }

    // Method to get userID
    public int getUserId() {
        String sql = "SELECT id FROM users WHERE username = ?"; // Query to get the user ID for the given username

        try (Connection conn = DataBaseConnection.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            pstmt.setString(1, this.username); // Set the username parameter
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
