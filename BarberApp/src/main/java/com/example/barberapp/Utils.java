package com.example.barberapp;

// Utils class to defined helper methods to connect main method from main class

import com.example.barberapp.database.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/* along this project, they are several methods that I had to create because I was getting a new idea on how to do something
* or just because it was needed to perform a task and this class was aimed to place all these methods. Because of time
* I could not take al those method to this class and then have a more organized project, but you could move them here
* if you would like to use this as a template. I would recommend to do so if you are working on similar or larger project*/

public class Utils {

    // method that gives you the amount of barbers, method not used, but it was planned to.
    public static int getBarberCount(int userId) {
        String sql = "SELECT COUNT(barbername) AS count FROM barbers WHERE user_id = ?";
        int count = 0;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId); // Set the userId parameter in the prepared statement

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("count"); // Retrieve the count from the result set
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return count; // Return the count
    }



}
