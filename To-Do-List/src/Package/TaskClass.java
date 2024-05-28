package Package;

// Class for defining the methods for assigning title, description, due date and status of the different tasks

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskClass {

    // internal variables of the class to initiate values
    private int userId;                        // create userId internal variable
    private String title;                      // create title internal variable
    private String description;                // create a description internal variable
    private String status;                     // create a status internal variable
    private Date deadline;                     // create a deadline internal variable

    // CONSTRUCTOR OF TASK MANAGER CLASS
    public TaskClass (int userId, String title, String description, String status, Date deadline) {

        // Private variables defined above as assigned the input values from TaskManagerClass everytime an instance of the class is created
        this.userId = userId;                               // the internal variable userId gets assigned the input value userId
        this.title = title;                                 // the internal variable title gets assigned the input value title
        this.description = description;                     // the internal variable description gets assigned the input value description
        this.status = status;                               // the internal variable status gets assigned the input value status
        this.deadline = deadline;                           // the internal variable deadline gets assigned the input value deadline
    }

    // INSERT THE NEW VALUES IN THE TASK TABLE CREATED WITH SQLite
    public void insert() {
        String sql = "INSERT INTO tasks(user_id, title, description, status, deadline) VALUES(?,?,?,?,?)";   // insert new row in the task table

        try (Connection conn = DataBaseConnectionClass.connect();             // connect with database through connect() method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {          // prepares a PreparedStatement object to execute the SQL statement provided
            pstmt.setInt(1, this.userId);                                  // set userId value in the table
            pstmt.setString(2, this.title);                                // set title in the table
            pstmt.setString(3, this.description);                          // set description in the table
            pstmt.setString(4, this.status);                               // set status in the table
            pstmt.setDate(5, this.deadline);                               // set deadline in the table
            pstmt.executeUpdate();                                            // update table
            System.out.println("Task inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}