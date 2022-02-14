module com.cg.lab1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.cg.lab1 to javafx.fxml;
    exports com.cg.lab1;
}