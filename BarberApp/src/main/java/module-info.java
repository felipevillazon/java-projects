module com.example.barberapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.barberapp to javafx.fxml;
    exports com.example.barberapp;
    exports com.example.barberapp.controllers;
    opens com.example.barberapp.controllers to javafx.fxml;
    exports com.example.barberapp.database;
    opens com.example.barberapp.database to javafx.fxml;

}