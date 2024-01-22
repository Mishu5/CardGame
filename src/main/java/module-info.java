module com.example.kierki {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.kierki.client to javafx.fxml;
    exports com.example.kierki.client;
}