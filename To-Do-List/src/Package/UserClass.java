package Package;

// In this class we defined the method for creating username, password, tasks assignments
import java.sql.*;

import java.util.Scanner;

public class UserClass {

    private String username;                // internal variable "username" of the class
    private String password;                // internal variable "password" of the class



    // CONSTRUCTOR OF THE CLASS, IT WILL INITIATE THE PRIVATE VARIABLE WITH THE INPUT VALUES
    public UserClass(String username, String password) {
        this.username = username;             // assign the private variable username with the username input
        this.password = password;             // assign the private variable password with the password input
    }





    // REGISTER METHOD, IT WILL CREATE A NEW ROW TO ADD THE NEW USERNAME AS PASSWORD TO THE USERS TABLE IN SQL
    public void register() {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";  // SQL to check if the username exists
        String insertUserSql = "INSERT INTO users(username, password) VALUES(?,?)";  // SQL to insert new user

        boolean usernameTaken = true;
        Scanner scanner = new Scanner(System.in);

        while (usernameTaken) {
            try (Connection conn = DataBaseConnectionClass.connect();  // get connection to the database
                 PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {  // initialize statement to check username

                checkStmt.setString(1, this.username);  // set the username parameter
                ResultSet rs = checkStmt.executeQuery();  // execute the query

                if (rs.next() && rs.getInt(1) > 0) {
                    // If the count is greater than 0, the username is taken
                    System.out.println("Username is taken. Please choose another one:");
                    this.username = scanner.nextLine();

                    System.out.println("Please enter your password:");
                    this.password = scanner.nextLine();

                } else {
                    // If the username is not taken, proceed with registration
                    usernameTaken = false;

                    try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {  // initialize statement to insert user
                        insertStmt.setString(1, this.username);  // set the new username
                        insertStmt.setString(2, this.password);  // set the new password
                        insertStmt.executeUpdate();  // update the users table
                        System.out.println("User registered.");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }





    // LOGIN WITH YOUR USERNAME AND PASSWORD IF THE USER NAME EXISTS
    public static int login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";    // from the users table select the input username and password

        try (Connection conn = DataBaseConnectionClass.connect();                  // connect to the database
             PreparedStatement pstmt = conn.prepareStatement(sql)) {               // initialize object
             pstmt.setString(1, username);                                       // assign first value to be the input username
             pstmt.setString(2, password);                                       // passing the first value to be the input password
             ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("User logged in with ID: " + userId);
                return userId;
            } else {
                System.out.println("Invalid username or password.");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }


    public static void userUpdate(Scanner access, int taskId){

        boolean userUpdate = true;

        while (userUpdate) {
            System.out.println("Enter a command:");
            System.out.println("1. update title");
            System.out.println("2. update description");
            System.out.println("3. update status");
            System.out.println("4. update deadline");
            System.out.println("5. exit");
            String command = access.nextLine();

            switch (command.toLowerCase()) {

                case "1":
                    System.out.println("Enter new title:");
                    String title = access.nextLine();
                    TaskManagerClass.updateTitle(taskId, title);
                    System.out.println("Title has been updated");

                    break;
                case "2":
                    System.out.println("Enter new description:");
                    String description = access.nextLine();
                    TaskManagerClass.updateDescription(taskId, description);
                    ;
                    System.out.println("Description has been updated");

                    break;

                case "3":
                    System.out.println("Enter new status:");
                    String status = access.nextLine();
                    TaskManagerClass.updateStatus(taskId, status);
                    ;
                    System.out.println("Status has been updated");

                    break;

                case "4":

                    Date deadline = null;
                    while (deadline == null) {
                        System.out.println("Enter new deadline (yyyy-MM-dd):");
                        String deadlineStr = access.nextLine();
                        try {
                            deadline = Date.valueOf(deadlineStr); // Convert the string to java.sql.Date
                            TaskManagerClass.updateDeadline(taskId, deadline);
                            System.out.println("Deadline has been updated");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }

                    break;

                case "exit":
                    System.out.println("Returning to menu...");
                    userUpdate = false;
                    break;
                default:
                    System.out.println("Unknown command. Please enter '1' for updating title, '2' for updating description, '3' for updating status, '4' for update deadline or 'exit'.");
                    break;

            }
        }

    }

}
