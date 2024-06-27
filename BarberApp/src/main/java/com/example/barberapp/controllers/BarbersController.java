package com.example.barberapp.controllers;

import com.example.barberapp.BarberApp;
import com.example.barberapp.BarberManager;
import com.example.barberapp.UserAccountManager;
import com.example.barberapp.database.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BarbersController implements Initializable {

    @FXML
    private AnchorPane barberAnchorPane;
    @FXML
    private Label barberNameLabel;
    @FXML
    private TextField newBarberNameText;
    @FXML
    private Button buttonAddBarber;
    @FXML
    private Button buttonRemoveBarber;
    @FXML
    private Pane paneBarberName;
    @FXML
    private Text errorMessage;
    @FXML
    private ListView<String> listViewBarbers;
    @FXML
    private Button buttonBarbersAvailability;
    @FXML
    private Button logoutButton;

    static ObservableList<String> dataList;


    // initialize th barber scene, values already in the database are load automatically when initializing this scene
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataList = FXCollections.observableArrayList();
        listViewBarbers.setItems(dataList);

        listViewBarbers.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setFont(new Font("Arial", 22)); // Set the font size to 20, you can change this
                        }
                    }
                };
            }
        });


        String username = UserContext.getUsername();
        String password = UserContext.getPassword();
        int userId = new UserAccountManager(username, password).getUserId();

        populateListView(userId);
    }

    // if you click on addBarber button and if text is not empty, this adds the new barber to the listView
    @FXML
    protected void onButtonAddBarberClick() {
        String username = UserContext.getUsername();
        String password = UserContext.getPassword();
        int userId = new UserAccountManager(username, password).getUserId();

        String barbername = newBarberNameText.getText().trim().toUpperCase();

        if (barbername.isEmpty()) {
            errorMessage.setText("Please enter a barber name");
            errorMessage.setVisible(true);
        } else {
            boolean isBarber = new BarberManager(barbername, userId).createBarber();

            if (isBarber) {
                errorMessage.setText("Barber already exists!");
                errorMessage.setVisible(true);
            } else {
                dataList.add(barbername);
                errorMessage.setVisible(false);
            }
        }
    }

    // if you click on removeBarber button and if text is not empty, this removes the barber from the listView
    @FXML
    protected void onButtonRemoveBarberClick() {
        String username = UserContext.getUsername();
        String password = UserContext.getPassword();
        int userId = new UserAccountManager(username, password).getUserId();

        String barbername = newBarberNameText.getText().trim();

        if (barbername.isEmpty()) {
            errorMessage.setText("Please enter a barber name");
            errorMessage.setVisible(true);
        } else {
            boolean isBarber = new BarberManager(barbername, userId).removeBarber();

            if (!isBarber) {
                errorMessage.setText("Barber does not exist or cannot be removed");
                errorMessage.setVisible(true);
            } else {
                dataList.remove(barbername);
                errorMessage.setVisible(false);
            }
        }
    }

    /* Main method of the class, this handles action on the elements from the listview*/
    @FXML
    protected void onListViewBarberClick() throws IOException {
        String selectedItem = listViewBarbers.getSelectionModel().getSelectedItem();

        if (selectedItem != null && !selectedItem.isEmpty()) {
            Stage stage = (Stage) listViewBarbers.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("month-appointment-view.fxml")));
            stage.setTitle("Barbers");
            stage.setScene(new Scene(root));
            stage.show();
            UserContext.setBarbername(selectedItem);
        } else {
            System.out.println("Please select a valid item");
        }
    }

    /* in order to initialize the listview with the barbers already in the database,
     *  we need to get them from the database and add them to the listview at the beginning*/
    public static List<String> getBarbers(int userId) {
        List<String> dataList = new ArrayList<>();
        String sql = "SELECT barbername FROM barbers WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String data = rs.getString("barbername");
                    dataList.add(data);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dataList;
    }

    /* method to add the barbers to the database to the listview*/
    public static void populateListView(int userId) {
        List<String> barbers = getBarbers(userId);
        dataList.setAll(barbers);
    }


    /* button to log out from current user*/
    @FXML
    protected  void onLogoutButtonClick() throws IOException {

        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BarberApp.class.getResource("login-register-view.fxml")));
        stage.setScene(new Scene(root));
        stage.show();

    }


}




