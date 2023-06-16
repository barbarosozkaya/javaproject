module com.example.checkersg {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;

    opens com.example.checkersg to javafx.fxml;

    exports com.example.checkersg;
}
