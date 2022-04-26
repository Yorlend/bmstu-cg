module bmstu.cg.lab5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens bmstu.cg.lab5 to javafx.fxml;
    exports bmstu.cg.lab5;
}