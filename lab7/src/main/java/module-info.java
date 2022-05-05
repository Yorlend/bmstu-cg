module bmstu.cg.lab7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens bmstu.cg.lab7 to javafx.fxml;
    exports bmstu.cg.lab7;
}