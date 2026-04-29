module com.example.domacinotinstaledfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.domacinotinstaledfx to javafx.fxml;
    exports com.example.domacinotinstaledfx;
}