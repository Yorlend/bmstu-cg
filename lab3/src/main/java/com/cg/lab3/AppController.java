package com.cg.lab3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox<String> algChoiceBox;

    @FXML
    private ChoiceBox<String> colorChoiceBox;

    @FXML
    private TextField xStartField;

    @FXML
    private TextField yStartField;

    @FXML
    private TextField xEndField;

    @FXML
    private TextField yEndField;

    @FXML
    private TextField rField;

    @FXML TextField quantityField;

    @FXML
    protected void onSunButtonClicked() {

        if (rField.getText().isEmpty() || quantityField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {

            int radius = (int) Math.round(Double.parseDouble(rField.getText()));
            int quantity = (int) Math.round(Double.parseDouble(quantityField.getText()));

            int xc = (int) canvas.getWidth() / 2;
            int yc = (int) canvas.getHeight() / 2;

            double theta = 0;
            double delta = 2 * Math.PI / quantity;
            for (int i = 0; i < quantity; i++) {
                double sin = Math.sin(theta);
                double cos = Math.cos(theta);

                int x = (int) (radius * cos) + xc;
                int y = (int) (radius * sin) + yc;

                algFuncs.get(algChoiceBox.getValue()).accept(new Point(xc, yc), new Point(x, y),
                        colors.get(colorChoiceBox.getValue()));

                theta += delta;
            }
        } catch (NumberFormatException e) {

        }
    }

    @FXML
    protected void onDrawLineButtonClicked() {

        if (xStartField.getText().isEmpty() || yStartField.getText().isEmpty() ||
                xEndField.getText().isEmpty() || yEndField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {

            int xn = (int) Math.round(Double.parseDouble(xStartField.getText()));
            int yn = (int) Math.round(Double.parseDouble(yStartField.getText()));
            int xk = (int) Math.round(Double.parseDouble(xEndField.getText()));
            int yk = (int) Math.round(Double.parseDouble(yEndField.getText()));

            algFuncs.get(algChoiceBox.getValue()).accept(new Point(xn, yn), new Point(xk, yk),
                    colors.get(colorChoiceBox.getValue()));
        } catch (NumberFormatException e) {

        }
    }

    @FXML
    protected void onCleanButtonClicked() {

        var gc = canvas.getGraphicsContext2D();

        gc.clearRect(
                0, 0,
                canvas.getWidth(), canvas.getHeight()
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderer = new Renderer(canvas.getGraphicsContext2D().getPixelWriter());

        algChoiceBox.setItems(FXCollections.observableArrayList(
                "ЦДА", "Брезенхем", "Целочис. Брезенхем", "Брезенхем с устранением ступенчатости",
                "Ву", "Библиотечный"
        ));
        algChoiceBox.applyCss();
        algChoiceBox.setValue("Библиотечный");

        colorChoiceBox.setItems(FXCollections.observableArrayList(
                "Черный", "Синий", "Красный", "Зеленый", "Цвет фона"
        ));
        colorChoiceBox.applyCss();
        colorChoiceBox.setValue("Черный");

        algFuncs = Map.of(
                "ЦДА", renderer::DDA,
                "Брезенхем", renderer::BresenhamFloat,
                "Целочис. Брезенхем", renderer::BresenhamInt,
                "Брезенхем с устранением ступенчатости", renderer::BresenhamAA,
                "Ву", renderer::Wu,
                "Библиотечный", this::library
        );

        colors = Map.of(
                "Черный", Color.BLACK,
                "Синий", Color.BLUE,
                "Красный", Color.RED,
                "Зеленый", Color.GREEN,
                "Цвет фона", Color.rgb(244, 244, 244)
        );
    }

    private void formErrorMessage(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Некорректный ввод");
        errorAlert.setContentText(text);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.showAndWait();
    }

    private void library(Point start, Point end, Color color) {

        var gc = canvas.getGraphicsContext2D();

        gc.setImageSmoothing(false);
        gc.setStroke(color);
        gc.setLineWidth(1);

        gc.beginPath();

        gc.moveTo(start.getX() + 0.5, start.getY() + 0.5);
        gc.lineTo(end.getX() + 0.5, end.getY() + 0.5);

        gc.stroke();
    }

    private Renderer renderer;
    private Map<String, TriConsumer<Point, Point, Color>> algFuncs;
    private Map<String, Color> colors;
}