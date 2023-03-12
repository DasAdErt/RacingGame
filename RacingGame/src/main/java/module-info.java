module com.example.racinggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.racinggame to javafx.fxml;
    exports com.example.racinggame;
}