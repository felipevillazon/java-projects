package com.example.barberapp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseManager {

    // USE SQLlite TO CREATE TABLES FOR THE USERS, BARBERS and APPOINTMENTS
    public static void createNewTables() {

        // SQL statement for creating a users table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (\n"    // create table if it does not exist or open it otherwise
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"     // create a column with the column number identifier
                + "    username TEXT NOT NULL UNIQUE,\n"            // create a column for the username
                + "    password TEXT NOT NULL\n"                    // create a column for the password
                + ");";                                             // close table

        // SQL statement for creating a barber table
        String sqlBarbers = "CREATE TABLE IF NOT EXISTS barbers (\n"    // create table if it does not exist or open it otherwise
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"         // create a column with the column number identifier
                + "    user_id INTEGER NOT NULL,\n"                     // create a column userid number to link task to different users
                + "    barbername TEXT NOT NULL UNIQUE,\n"              // create a column for the username
                + "    FOREIGN KEY (user_id) REFERENCES users(id)\n"    // establishes that user_id corresponds to a key in another table
                + ");";                                                 // close table


        // SQL statement for creating an appointment table
        String sqlAppointment = "CREATE TABLE IF NOT EXISTS appointments (\n"     // create table if it does not exist or open it otherwise
                + "    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"                   // create a column with the column number identifier
                + "    user_id INTEGER NOT NULL,\n"                               // create a column userid number to link task to different users
                + "    barber_id INTEGER NOT NULL,\n"                             // create a column userid number to link task to different users
                + "    date DATE NOT NULL,\n"                                     // create a column for task tittle
                + "    time TIME NOT NULL,\n"                                     // create a column for task description
                + "    is_taken BOOLEAN NOT NULL CHECK (is_taken IN (0, 1)) DEFAULT 0,\n"// create a column for task status
                + "    clientName TEXT,\n"
                + "    FOREIGN KEY (user_id) REFERENCES users(id),\n"             // establishes that user_id corresponds to a key in another table
                + "    FOREIGN KEY (barber_id) REFERENCES barbers(id)\n"          // establishes that user_id corresponds to a key in another table
                + ");";                                                           // close table

        try (Connection conn = DataBaseConnection.connect();     // try to connect with the database through the DataBaseConnection class using the connect method
             Statement stmt = conn.createStatement()) {               // interface and it is used to sending a SQL query to the database

            stmt.execute(sqlUsers);                                   // create user table
            stmt.execute(sqlBarbers);                                 // create barber table
            stmt.execute(sqlAppointment);                             // create appointment table
            //System.out.println("Tables created or already exist.");   // confirm that table have been created
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}