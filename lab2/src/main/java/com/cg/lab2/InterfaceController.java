package com.cg.lab2;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InterfaceController {
    @FXML
    private Canvas canvas;

    @FXML
    private TextArea logs;

    @FXML
    private TextField dxField;

    @FXML
    private TextField dyField;

    @FXML
    private TextField kxField;

    @FXML
    private TextField kyField;

    @FXML
    private TextField thetaField;

    @FXML
    protected void onMoveButtonClicked() {
        logs.appendText("Произведен перенос.\n");
    }

    @FXML
    protected void onResizeButtonClicked() {
        logs.appendText("Произведено масштабирование.\n");
    }

    @FXML
    protected void onRotateButtonClicked() {
        logs.appendText("Произведен поворот.\n");
    }

    @FXML
    protected void onRewindButtonClicked() {
        /// TODO: delete last line;
        logs.redo();
    }

    @FXML
    protected void onResetButtonClicked() {
        logs.clear();
    }
}