module com.example.numdio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.numdio to javafx.fxml;
    exports com.example.numdio;
}