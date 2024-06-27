package com.example.barberapp.controllers;

import com.example.barberapp.BarberApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class MonthAppoitnmentController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text textYear;

    @FXML
    private Text textMonth;

    @FXML
    private FlowPane flowPanecalendar;
    @FXML
    private Button buttonBackMonth;
    @FXML
    private Button buttonForwardMonth;
    @FXML
    private Button buttonBackToBarbers;
    @FXML
    private Text errorMessage;

    /* initialize the month calendar*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    /* button that takes you back one month*/
    @FXML
    void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        flowPanecalendar.getChildren().clear();
        drawCalendar();
    }

    /* button that takes you one month forward */
    @FXML
    void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        flowPanecalendar.getChildren().clear();
        drawCalendar();
    }

    /* main method of the class. create a grid that represents a calendar. If any of the slots from the calendar
    * are click, the method also handles the initialization of the new scene, in this case, the daily calendar */
    private void drawCalendar() {
        textYear.setText(String.valueOf(dateFocus.getYear()));
        textMonth.setText(String.valueOf(dateFocus.getMonth()));

        UserContext.setMonth(dateFocus.getMonth());
        UserContext.setYear(dateFocus.getYear());
        UserContext.setDay(dateFocus.getDayOfMonth());

        double calendarWidth = flowPanecalendar.getPrefWidth();
        double calendarHeight = flowPanecalendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = flowPanecalendar.getHgap();
        double spacingV = flowPanecalendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength();
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        ZonedDateTime currentZonedDateTime = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate, 0, 0, 0, 0, dateFocus.getZone());
                        Text date = new Text(String.valueOf(currentDate));
                        date.setFont(new Font(20));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        //date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        if (currentZonedDateTime.isBefore(today.minusDays(1))) {
                            rectangle.setFill(Color.LIGHTGREY);
                            rectangle.setOnMouseClicked(mouseEvent -> {
                                String errorDate = "It is too late for this!";
                                System.out.print(errorDate);
                                System.out.print("\n");
                                errorMessage.setText(errorDate);
                                errorMessage.setVisible(true);
                            });

                        } else {
                            rectangle.setOnMouseClicked(event -> {
                                rectangle.setFill(Color.LIGHTGREEN); // Change the rectangle color to green
                                try {
                                    Stage stage = (Stage) rectangle.getScene().getWindow();
                                    UserContext.setDay(Integer.parseInt(date.getText()));

                                    Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("daily-appointment-view.fxml")));
                                    stage.setTitle("Day Availability");
                                    stage.setScene(new Scene(root));
                                    stage.show();

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        //rectangle.setStroke(Color.BLUE);
                        rectangle.setFill(Color.DARKCYAN);
                        rectangle.setOpacity(0.5);
                    }
                }
                flowPanecalendar.getChildren().add(stackPane);
            }
        }

    }

    /* back button to go back to the list of all barbers */
    @FXML
    protected void onButtonBackToBarbersClick() throws IOException {

        Stage stage = (Stage) buttonBackToBarbers.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("barbers-view.fxml")));
        stage.setTitle("Barbers");
        stage.setScene(new Scene(root));
        stage.show();

    }

    /* method that helps to see if a given date has already passed */
    public static boolean hasDatePassed(ZonedDateTime dateFocus) {
        ZonedDateTime currentDate = ZonedDateTime.now();
        return dateFocus.isBefore(currentDate);
    }
}
