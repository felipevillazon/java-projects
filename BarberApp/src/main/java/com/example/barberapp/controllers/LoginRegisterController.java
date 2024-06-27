package com.example.barberapp.controllers;

import com.example.barberapp.BarberApp;
import com.example.barberapp.UserAccountManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginRegisterController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonRegister;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label registerLabel;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private Text errorMessage;


    /* login button. if a username and password is given. */
    @FXML
    protected void onButtonLoginClick() throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();

        UserContext.setUsername(username);
        UserContext.setPassword(password);

        boolean islogin = new UserAccountManager(username, password).login();

        if (username.isEmpty()){
            String errorUsername = "Please enter a username";
            errorMessage.setText(errorUsername);
            errorMessage.setVisible(true);
        } else if (password.isEmpty()) {
            String errorPassword = "Please enter your password";
            errorMessage.setText(errorPassword);
            errorMessage.setVisible(true);
        } else if (!islogin) {
            String errorLogin = "Wrong username or password";
            System.out.print(errorLogin);
            System.out.print("\n");
            errorMessage.setText(errorLogin);
            errorMessage.setVisible(true);
        } else {
            System.out.print("You are in!");
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("barbers-view.fxml")));
            stage.setTitle("Barbers");
            stage.setScene(new Scene(root));
            stage.show();

        }
    }

    /* if new user, use this button to register */
    @FXML
    protected void onButtonRegisterClick() throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();


        if (username.isEmpty()){
            String errorUsername = "Please enter a valid username";
            errorMessage.setText(errorUsername);
            errorMessage.setVisible(true);
        } else if (password.isEmpty()) {

            String errorPassword = "Please enter a valid password";
            errorMessage.setText(errorPassword);
            errorMessage.setVisible(true);
        } else {

            boolean isRegister = new UserAccountManager(username, password).register();

            if (!isRegister) {
                String errorRegister = "Username already exist!";
                System.out.print(errorRegister);
                System.out.print("\n");
                errorMessage.setText(errorRegister);
                errorMessage.setVisible(true);
            } else {
                System.out.print("You are in!");
                Stage stage = (Stage) buttonLogin.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("barbers-view.fxml")));
                stage.setTitle("Barbers");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

}