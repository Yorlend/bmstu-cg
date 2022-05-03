module bmstu.cg.lab6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens bmstu.cg.lab6 to javafx.fxml;
    exports bmstu.cg.lab6;
}