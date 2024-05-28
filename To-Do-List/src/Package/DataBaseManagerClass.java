package Package;

import java.sql.*;


public class DataBaseManagerClass {

    // USE SQLlite TO CREATE TABLES FOR THE USERS AND THE TASKS
    public static void createNewTables() {

        // SQL statement for creating a users table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (\n"    // create table if it does not exist or open it otherwise
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"     // create a column with the column number identifier
                + "    username TEXT NOT NULL UNIQUE,\n"            // create a column for the username
                + "    password TEXT NOT NULL\n"                    // create a column for the password
                + ");";                                             // close table

        // SQL statement for creating a tasks table with a deadline column
        String sqlTasks = "CREATE TABLE IF NOT EXISTS tasks (\n"      // create table if it does not exist or open it otherwise
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"       // create a column with the column number identifier
                + "    user_id INTEGER NOT NULL,\n"                   // create a column userid number to link task to different users
                + "    title TEXT NOT NULL,\n"                        // create a column for task tittle
                + "    description TEXT,\n"                           // create a column for task description
                + "    status TEXT NOT NULL,\n"                       // create a column for task status
                + "    deadline DATE,\n"                              // create a column for task deadline
                + "    FOREIGN KEY (user_id) REFERENCES users(id)\n"  // establishes that user_id corresponds to a key in another table
                + ");";                                               // close table

        try (Connection conn = DataBaseConnectionClass.connect();     // try to connect with the database through the DataBaseConnection class using the connect method
             Statement stmt = conn.createStatement()) {               // interface and it is used to sending a SQL query to the database

            stmt.execute(sqlUsers);                                   // create user table
            stmt.execute(sqlTasks);                                   // create task table
            //System.out.println("Tables created or already exist.");   // confirm that table have been created
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    // METHOD TO PRINT OUT ALL THE TASKS FOR A GIVEN USER
    public static void selectAllTasksForUser(int userId) {
        String sql = "SELECT id, title, description, status, deadline FROM tasks WHERE user_id = ?";   // select the different entries from the task table

        try (Connection conn = DataBaseConnectionClass.connect();                  // connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);                                               // set the user ID parameter
            ResultSet rs = pstmt.executeQuery();                                   // execute the query and get the result set

            // Print the headers
            System.out.printf("%-5s %-20s %-30s %-10s %-10s%n", "ID", "Title", "Description", "Status", "Deadline");

            // loop through the result set
            while (rs.next()) {                                                    // iterate over each row in the result set
                System.out.printf("%-5d %-20s %-30s %-10s %-10s%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("deadline"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // METHOD TO GET THE USERID FROM THE USERNAME
    public static int getUserId(String username) {
        String sql = "SELECT id FROM users WHERE username = ?"; // Query to get the user ID for the given username

        try (Connection conn = DataBaseConnectionClass.connect(); // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            pstmt.setString(1, username); // Set the username parameter
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                int userId = rs.getInt("id"); // Get the user ID from the result set
                //System.out.println("User ID found: " + userId);
                return userId; // Return the user ID
            } else {
                //System.out.println("Username not found.");
                return -1; // Return -1 if the username is not found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Print the SQL exception message
            return -1; // Return -1 in case of an SQL exception
        }
    }

    public static void allUsersWithoutPassword() {
        String sql = "SELECT id, username FROM users";   // Select user ID and username from the users table

        try (Connection conn = DataBaseConnectionClass.connect();                  // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();                                   // Execute the query and get the result set

            // Print the headers
            System.out.printf("%-5s %-20s%n", "ID", "Username");

            // Loop through the result set
            while (rs.next()) {                                                    // Iterate over each row in the result set
                System.out.printf("%-5d %-20s%n",
                        rs.getInt("id"),
                        rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static boolean isTaskBelongToUser(int userId, int taskId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            // If the query returns a count greater than 0, the task belongs to the user
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false by default if an exception occurs or no match is found
    }

}

