module com.exemple.codehive {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.exemple.codehive to javafx.fxml;
    opens controller to javafx.fxml;
    exports com.exemple.codehive;



}