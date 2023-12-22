module com.exemple.codehive {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.exemple.codehive to javafx.fxml;
    exports com.exemple.codehive;
}