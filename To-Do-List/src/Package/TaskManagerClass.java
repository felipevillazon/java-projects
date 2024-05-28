package Package;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskManagerClass {

    // UPDATE THE SELECTED TASK
    public static void updateDeadline(int taskId, Date newdeadline) {
        String sql = "UPDATE tasks SET deadline = ? WHERE id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, newdeadline);
            pstmt.setInt(2, taskId); // Set the task ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateTitle(int taskId, String newtitle) {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newtitle);
            pstmt.setInt(2, taskId); // Set the task ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateDescription(int taskId, String newdescription) {
        String sql = "UPDATE tasks SET description = ? WHERE id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newdescription);
            pstmt.setInt(2, taskId); // Set the task ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateStatus(int taskId, String newstatus) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newstatus);
            pstmt.setInt(2, taskId); // Set the task ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(int taskId) {

        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DataBaseConnectionClass.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setInt(1, taskId);
             pstmt.executeUpdate();
             System.out.println("Task deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}

