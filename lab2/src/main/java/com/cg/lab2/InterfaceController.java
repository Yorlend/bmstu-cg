package com.cg.lab2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.transform.Affine;

import java.net.URL;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {
    @FXML
    private Canvas canvas;

    @FXML
    private TextArea logs;

    @FXML
    private TextField dxField;

    @FXML
    private TextField dyField;

    @FXML
    private Label centerLabel;

    @FXML
    private TextField cxField;

    @FXML
    private TextField cyField;

    @FXML
    private TextField kxField;

    @FXML
    private TextField kyField;

    @FXML
    private TextField thetaField;

    private final OperationManager operationManager = new OperationManager();

    private void formErrorMessage(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Некорректный ввод");
        errorAlert.setContentText(text);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.showAndWait();
    }

    @FXML
    protected void onMoveButtonClicked() {

        if (dxField.getText().isEmpty() || dyField.getText().isEmpty()) {

            formErrorMessage("Поля ввода dx, dy не могут быть пустыми.");

            return;
        }

        try {

            double dx = Double.parseDouble(dxField.getText());
            double dy = Double.parseDouble(dyField.getText());

            var operation = new Affine(1, 0, dx, 0, 1, dy);
            operationManager.addOperation(operation);

            logs.appendText(String.format("Произведен перенос: " +
                    "dx = %.2f, dy = %.2f\n", dx, dy));
            renderFigure();
        } catch (NumberFormatException e) {

            formErrorMessage("Ввод содержит недопустимые символы.");
        }

    }

    @FXML
    protected void onResizeButtonClicked() {

        double cx = 0, cy = 0;

        if (kxField.getText().isEmpty() || kyField.getText().isEmpty()) {

            formErrorMessage("Поля ввода kx, ky не могут быть пустыми.");

            return;
        }

        try {

            cx = cxField.getText().isEmpty() ? 0 : Double.parseDouble(cxField.getText());
            cy = cyField.getText().isEmpty() ? 0 : Double.parseDouble(cyField.getText());
            double kx = Double.parseDouble(kxField.getText());
            double ky = Double.parseDouble(kyField.getText());

            var operation = new Affine(1, 0, -cx, 0, 1, -cy);
            operation.prepend(new Affine(kx, 0, 0, 0, ky, 0));
            operation.prepend(new Affine(1, 0, cx, 0, 1, cy));

            operationManager.addOperation(operation);

            logs.appendText(String.format("Произведено масштабирование: " +
                    "центр (%.2f, %.2f), kx = %.2f, ky = %.2f\n", cx, cy, kx, ky));
            renderFigure();
        } catch (NumberFormatException e) {

            formErrorMessage("Ввод содержит недопустимые символы.");
        }

    }

    @FXML
    protected void onRotateButtonClicked() {

        double cx = 0, cy = 0;

        if (thetaField.getText().isEmpty()) {
            formErrorMessage("Поле ввода угла не может быть пустым.");
        }

        try {

            cx = cxField.getText().isEmpty() ? 0 : Double.parseDouble(cxField.getText());
            cy = cyField.getText().isEmpty() ? 0 : Double.parseDouble(cyField.getText());

            double theta = Double.parseDouble(thetaField.getText());
            double thetaRad = theta * Math.PI / 180;
            double sin = Math.sin(thetaRad);
            double cos = Math.cos(thetaRad);

            var operation = new Affine(1, 0, -cx, 0, 1, -cy);
            operation.prepend(new Affine(cos, -sin, 0, sin, cos, 0));
            operation.prepend(new Affine(1, 0, cx, 0, 1, cy));

            logs.appendText(String.format("Произведен поворот на угол " +
                    "%.2f\n", theta));

            operationManager.addOperation(operation);
            renderFigure();
        } catch (NumberFormatException e) {
            formErrorMessage("Ввод содержит недопустимые символы.");
        }
    }

    @FXML
    protected void onRewindButtonClicked() {

        String logInfo = logs.getText();

        int truncateIndex = logInfo.lastIndexOf('\n', logInfo.length() - 2);
        if (truncateIndex == -1 && !logInfo.isEmpty()) {
            logs.clear();
        } else if (!logInfo.isEmpty()) {
            logs.setText(logInfo.substring(0, truncateIndex));
        }

        operationManager.rewindTransformation();
        renderFigure();
    }

    @FXML
    protected void onResetButtonClicked() {

        operationManager.resetTransformation();
        renderFigure();

        logs.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graphicsContext = canvas.getGraphicsContext2D();
        centerLabel.setText(String.format("Центр холста: (%.2f, %.2f)", canvas.getWidth() / 2,
                canvas.getHeight() / 2));

        renderFigure();
    }

    private void renderFigure() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        centerLabel.setText(String.format("Центр холста: (%.2f, %.2f)", canvas.getWidth() / 2,
                canvas.getHeight() / 2));

        graphicsContext.save();
        double kx = FIGURE_WIDTH / 8;
        double ky = -FIGURE_HEIGHT / 15;
        double dx = canvas.getWidth() / 2 - 4.5 * kx;
        double dy = canvas.getHeight() / 2 - 7.5 * ky;
        graphicsContext.transform(operationManager.getTransformation());
        graphicsContext.transform(kx, 0, 0, ky, dx, dy);
        graphicsContext.setLineWidth(3 / Math.min(FIGURE_WIDTH / 8, FIGURE_HEIGHT / 15));
        graphicsContext.beginPath();

        // head
        graphicsContext.strokeOval(2, 9, 5, 5);
        graphicsContext.strokeOval(3, 12, 1, 1);
        graphicsContext.strokeOval(5, 12, 1, 1);

        graphicsContext.arc(4.5, 11, 1.5, 1, 0, 180);

        graphicsContext.moveTo(2, 15);
        graphicsContext.lineTo(3, 15);
        graphicsContext.lineTo(4.5,13);
        graphicsContext.lineTo(6, 15);
        graphicsContext.lineTo(7, 15);

        // body
        graphicsContext.strokeOval(2, 2, 5, 7);
        graphicsContext.moveTo(3, 5);
        graphicsContext.lineTo(1, 9);
        graphicsContext.lineTo(3, 7);
        graphicsContext.lineTo(3, 5);

        graphicsContext.moveTo(6, 5);
        graphicsContext.lineTo(6, 7);
        graphicsContext.lineTo(8, 9);
        graphicsContext.lineTo(6, 5);

        // legs
        graphicsContext.moveTo(3.5, 2.25);
        graphicsContext.lineTo(3.5, 0);
        graphicsContext.lineTo(2, 0);

        graphicsContext.moveTo(5.5, 2.25);
        graphicsContext.lineTo(5.5, 0);
        graphicsContext.lineTo(7, 0);

        graphicsContext.stroke();
        graphicsContext.restore();
    }

    GraphicsContext graphicsContext;

    static final double FIGURE_WIDTH = 20.0 * 8;
    static final double FIGURE_HEIGHT = 20.0 * 15;
}