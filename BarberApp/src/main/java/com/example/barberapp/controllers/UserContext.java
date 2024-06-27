package com.example.barberapp.controllers;

import java.time.DayOfWeek;
import java.time.Month;

/* in general, this class contains setters and getters. Mainly to get and set values for the different users, barbers
* and appointment information that we use between classes*/

public class UserContext {

    private static String username;
    private static String password;

    private static int day;
    private static Month month;
    private static int year;

    private static String barbername;

    public static String getUsername() { return username;  }
    public static void setUsername(String username) { UserContext.username = username; }

    public static String getPassword() { return password;  }
    public static void setPassword(String password) { UserContext.password = password; }

    public static int getDay() { return day;  }
    public static void setDay(int day) { UserContext.day = day; }

    public static Month getMonth() { return month;  }
    public static void setMonth(Month month) { UserContext.month = month; }

    public static int getYear() { return year;  }
    public static void setYear(int year) { UserContext.year = year; }

    public static String getBarbername() { return barbername;  }
    public static void setBarbername(String barbername) { UserContext.barbername = barbername; }


}
