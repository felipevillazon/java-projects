package com.example.barberapp;

import com.example.barberapp.database.DataBaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/* Main class of the project*/

public class BarberApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BarberApp.class.getResource("login-register-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 650, 600);
        stage.setTitle("Welcome to your BARBER APP!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        DataBaseManager.createNewTables();

        launch();

    }
}