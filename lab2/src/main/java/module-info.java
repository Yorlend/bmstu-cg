module com.cg.lab2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.cg.lab2 to javafx.fxml;
    exports com.cg.lab2;
}