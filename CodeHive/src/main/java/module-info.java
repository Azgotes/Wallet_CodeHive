module com.exemple.codehive {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.net.http;
    requires org.json;
    requires javafx.base;

    opens com.exemple.codehive to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;
    exports com.exemple.codehive;



}