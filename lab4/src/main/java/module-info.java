module com.cg.lab4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.cg.lab4 to javafx.fxml;
    exports com.cg.lab4;
}