module bmstu.cg.lab8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens bmstu.cg.lab8 to javafx.fxml;
    exports bmstu.cg.lab8;
}