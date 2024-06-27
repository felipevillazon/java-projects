package com.example.barberapp.controllers;

import com.example.barberapp.*;
import com.example.barberapp.database.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class DailyAppointmentController implements Initializable {

    @FXML
    private Button buttonBack;
    @FXML
    private FlowPane flowPaneCalendar;
    @FXML
    private Button buttonBackMonth;
    @FXML
    private Button buttonForwardMonth;
    @FXML
    private Text day1, day2, day3, day4, day5, day6, day7;
    @FXML
    private Text textDay;
    @FXML
    private Text textMonth;
    @FXML
    private Text t1, t2, t3, t4, t5, t6, t7;
    @FXML
    private Text barberName;
    @FXML
    private  Text errorMessage;

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    /* initialize daily grid and current working date*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int month = UserContext.getMonth().getValue();
        int year = UserContext.getYear();
        int dia = UserContext.getDay();

        dateFocus = ZonedDateTime.of(year, month, dia, 10, 0, 0, 0, ZonedDateTime.now().getZone());
        today = ZonedDateTime.now();

        drawHourlySchedule();

        textDay.setText(String.valueOf(dateFocus.getDayOfMonth()));
        textMonth.setText(String.valueOf(dateFocus.getMonth()));

        barberName.setText(UserContext.getBarbername());


    }

    /* main method of the class, it handles all related with the appointment of a single barber
    * it draws the grid with the time slow of a day and also show not only the select date you would like to get an
    * appointment but also the next 6 days (full week) in case all slots in desirable day is already full */
    @FXML
    private void drawHourlySchedule() {
        double calendarWidth = flowPaneCalendar.getPrefWidth();
        double calendarHeight = flowPaneCalendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = flowPaneCalendar.getHgap();
        double spacingV = flowPaneCalendar.getVgap();

        day1.setText(dateFocus.getDayOfWeek().toString());
        day2.setText(dateFocus.getDayOfWeek().plus(1).toString());
        day3.setText(dateFocus.getDayOfWeek().plus(2).toString());
        day4.setText(dateFocus.getDayOfWeek().plus(3).toString());
        day5.setText(dateFocus.getDayOfWeek().plus(4).toString());
        day6.setText(dateFocus.getDayOfWeek().plus(5).toString());
        day7.setText(dateFocus.getDayOfWeek().plus(6).toString());

        t1.setText(formatDate(dateFocus));
        t2.setText(formatDate(dateFocus.plusDays(1)));
        t3.setText(formatDate(dateFocus.plusDays(2)));
        t4.setText(formatDate(dateFocus.plusDays(3)));
        t5.setText(formatDate(dateFocus.plusDays(4)));
        t6.setText(formatDate(dateFocus.plusDays(5)));
        t7.setText(formatDate(dateFocus.plusDays(6)));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {

                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 9) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);

                stackPane.getChildren().add(rectangle);

                ZonedDateTime cellDate = dateFocus.plusDays(j);  // Calculate the specific date for this cell
                completeGrid(i,j,rectangle,stackPane,dateFocus);

                // Set the color and disable actions for past dates
                if (cellDate.isBefore(today.minusDays(1))) {

                    rectangle.setFill(Color.LIGHTGREY);
                    rectangle.setDisable(true);

                } else {

                    int finalI = i;
                    int finalJ = j;

                    rectangle.setOnMouseClicked(event -> {

                        java.sql.Date dateSQL;
                        java.util.Date dateUtil;
                        dateUtil = java.util.Date.from(dateFocus.plusDays(finalJ).toInstant());
                        dateSQL = new java.sql.Date(dateUtil.getTime());
                        //System.out.print(dateSQL);

                        LocalTime localTime = LocalTime.of((finalI + 10) % 24, 0); // Use modulo to handle 24 as 0
                        Time time = Time.valueOf(localTime);
                        //System.out.print(time);

                        String barbername = UserContext.getBarbername();
                        String username = UserContext.getUsername();
                        String password = UserContext.getPassword();

                        int userId = new UserAccountManager(username, password).getUserId();
                        int barberId = new BarberManager(barbername, userId).getBarberId();

                        if (event.getButton() == MouseButton.PRIMARY) {

                            boolean isFree = new AddAppointment(userId, barberId).addAppointment(dateSQL, time);

                            if (!isFree) {
                                errorMessage.setText("Slot already taken!");
                                errorMessage.setVisible(true);


                            } else {
                                errorMessage.setVisible(false);
                                TextInputDialog dialog = new TextInputDialog();
                                dialog.setTitle("Client Name");
                                dialog.setHeaderText("Enter the client's name for the appointment:");
                                Optional<String> result = dialog.showAndWait();

                                result.ifPresent(name -> {
                                    rectangle.setFill(Color.ORANGE);
                                    rectangle.setOpacity(0.7);
                                    Text clientName = new Text(name.toUpperCase());
                                    clientName.setFont(new Font(14));
                                    stackPane.getChildren().add(clientName);
                                    boolean isName = new AddAppointment(userId, barberId).addClientName(dateSQL, time, name.toUpperCase());

                                    System.out.print(isName);


                                    //System.out.println(cellDate.getDayOfWeek());
                                    //System.out.println(cellDate.getDayOfMonth());
                                    //System.out.println(timeSlot)
                                });
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (rectangle.isManaged()){

                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Cancel Appointment");
                                alert.setHeaderText("Are you sure you want to cancel this appointment?");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {

                                    int appointmentId = new AddAppointment(userId,barberId).getAppointmentId(dateSQL, time);
                                    boolean isCanceled = new RemoveAppointment(userId, barberId, appointmentId).cancelAppointment();
                                    if (isCanceled) {
                                        rectangle.setFill(Color.DARKCYAN);
                                        stackPane.getChildren().removeIf(node -> node instanceof Text);
                                    }
                                }
                            }
                        }

                    });
                }

                flowPaneCalendar.getChildren().add(stackPane);
            }
        }
    }

    /* help to bring the working date to a DD-MM-YY format*/
    private String formatDate(ZonedDateTime date) {
        return date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
    }


    /* button that takes you back to the month calendar */
    @FXML
    protected void onButtonBackClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("month-appointment-view.fxml")));
        stage.setTitle("Month Calendar");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /* go one day button to see shift to an extra day in the daily calendar */
    @FXML
    protected void backOneDay() {
        dateFocus = dateFocus.minusDays(1);
        flowPaneCalendar.getChildren().clear();
        textDay.setText(String.valueOf(dateFocus.getDayOfMonth()));
        textMonth.setText(String.valueOf(dateFocus.getMonth()));
        drawHourlySchedule();
    }

    /* go back button to move to different days*/
    @FXML
    protected void forwardOneDay() {
        dateFocus = dateFocus.plusDays(1);
        flowPaneCalendar.getChildren().clear();
        textDay.setText(String.valueOf(dateFocus.getDayOfMonth()));
        textMonth.setText(String.valueOf(dateFocus.getMonth()));
        drawHourlySchedule();
    }


    /* second most important method of the class. It takes the data already in the database and help to fill the already
     * slots at the moment of initializing the scene of the daily availability of a barber */
    public static void completeGrid(int i, int j, Rectangle rectangle, StackPane stackPane, ZonedDateTime dateFocus) {

        java.sql.Date dateSQL;
        java.util.Date dateUtil;
        dateUtil = java.util.Date.from(dateFocus.plusDays(j).toInstant());
        dateSQL = new java.sql.Date(dateUtil.getTime());

        LocalTime localTime = LocalTime.of((i+10) % 24, 0); // Use modulo to handle 24 as 0
        Time time = Time.valueOf(localTime);

        String barbername = UserContext.getBarbername();
        String username = UserContext.getUsername();
        String password = UserContext.getPassword();

        int userId = new UserAccountManager(username, password).getUserId();
        int barberId = new BarberManager(barbername, userId).getBarberId();

        String checkAppointmentSql = "SELECT COUNT(*) FROM appointments WHERE user_id = ? AND barber_id = ? AND date = ? AND time = ? AND is_taken = 1";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkAppointmentSql)) {

            // Check if the date and time already exist
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, barberId);
            checkStmt.setDate(3, dateSQL);
            checkStmt.setTime(4, time);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {

                rectangle.setFill(Color.ORANGE);
                rectangle.setOpacity(0.7);
                int appointmentId = new AddAppointment(userId, barberId).getAppointmentId(dateSQL, time);
                String name = new AddAppointment(userId,barberId).getClientName(appointmentId);
                System.out.print(name);
                System.out.print("\n");
                if (name != null) {
                    Text clientName = new Text(name);
                    clientName.setFont(new Font(14));
                    stackPane.getChildren().add(clientName);
                }

            } else {

                rectangle.setFill(Color.DARKCYAN);
                rectangle.setOpacity(0.7);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





}
