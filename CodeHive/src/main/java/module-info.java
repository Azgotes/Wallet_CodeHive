module com.example.codehive {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.codehive to javafx.fxml;
    exports com.example.codehive;
}