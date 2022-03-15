module com.cg.lab3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.cg.lab3 to javafx.fxml;
    exports com.cg.lab3;
}