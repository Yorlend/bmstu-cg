package com.cg.lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab canvasTab, chartTab;

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox<String> algChoiceBox, colorChoiceBox;

    @FXML
    private TextField xCenterField, yCenterField, rField, aField, bField;

    @FXML
    private TextField quantityField, aStartField, bStartField, aDeltaField, bDeltaField, rStartField, rDeltaField;

    @FXML
    private LineChart<Integer, Long> circleChart, ellipseChart;

    @FXML
    protected void onCircleTimeAnalysisButtonClicked() {
        tabPane.getSelectionModel().select(chartTab);

        var list = circleChart.getData();
        if (list.isEmpty()) {
            for (var alg : AlgTimer.circMap.keySet().stream().sorted().toList()) {
                var series = new XYChart.Series<Integer, Long>();
                list.add(series);
                series.setName(alg);
            }

            circleChart.setCreateSymbols(false);
            circleChart.setTitle("Зависимость времени отрисовки окружности от радиуса");
            circleChart.setAnimated(false);
        }

        for (var series : list) {
            series.getData().clear();

            int rk = 200, dr = 10;
            var center = new Point(430, 430);
            var algo = AlgTimer.circMap.get(series.getName());
            for (int r = 10; r < rk; r += dr) {
                long time = algo.apply(center, r, Color.BLACK);

                series.getData().add(new XYChart.Data<Integer, Long>(r, time));
            }
        }
    }

    @FXML
    protected void onEllipseTimeAnalysisButtonClicked() {
        tabPane.getSelectionModel().select(chartTab);

        var list = ellipseChart.getData();
        if (list.isEmpty()) {
            for (var alg : AlgTimer.ellMap.keySet().stream().sorted().toList()) {
                var series = new XYChart.Series<Integer, Long>();
                list.add(series);
                series.setName(alg);
            }

            ellipseChart.setCreateSymbols(false);
            ellipseChart.setTitle("Зависимость времени отрисовки эллипса от суммы полуосей");
            ellipseChart.setAnimated(false);
        }

        for (var series : list) {
            series.getData().clear();

            int ak = 300, da = 10;
            int bk = 100, db = 5;
            var center = new Point(430, 430);
            var algo = AlgTimer.ellMap.get(series.getName());
            for (int a = 10, b = 5; a < ak && b < bk; a += da, b += db) {
                long time = algo.apply(center, a, b, Color.BLACK);

                series.getData().add(new XYChart.Data<Integer, Long>(a + b, time));
            }
        }
    }

    @FXML
    protected void onDrawCircleButtonClicked() {
        tabPane.getSelectionModel().select(canvasTab);

        if (xCenterField.getText().isEmpty() || yCenterField.getText().isEmpty() ||
            rField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {
            int xc = (int) Math.round(Double.parseDouble(xCenterField.getText()));
            int yc = (int) Math.round(Double.parseDouble(yCenterField.getText()));
            int r = (int) Math.round(Double.parseDouble(rField.getText()));

            circFuncs.get(algChoiceBox.getValue()).accept(new Point(xc, yc), r,
                    colors.get(colorChoiceBox.getValue()));
        } catch (NumberFormatException e) {
            formErrorMessage(e.getMessage());
        }
    }

    @FXML
    protected void onDrawEllipseButtonClicked() {
        tabPane.getSelectionModel().select(canvasTab);

        if (xCenterField.getText().isEmpty() || yCenterField.getText().isEmpty() ||
                aField.getText().isEmpty() || bField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {
            int xc = (int) Math.round(Double.parseDouble(xCenterField.getText()));
            int yc = (int) Math.round(Double.parseDouble(yCenterField.getText()));
            int a = (int) Math.round(Double.parseDouble(aField.getText()));
            int b = (int) Math.round(Double.parseDouble(bField.getText()));

            ellFuncs.get(algChoiceBox.getValue()).accept(new Point(xc, yc), a, b,
                    colors.get(colorChoiceBox.getValue()));
        } catch (NumberFormatException e) {
            formErrorMessage(e.getMessage());
        }
    }

    @FXML
    protected void onCircleSpectreButtonClicked() {
        if (quantityField.getText().isEmpty() || rStartField.getText().isEmpty() ||
            rDeltaField.getText().isEmpty() || xCenterField.getText().isEmpty() || yCenterField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());

            int xc = (int) Math.round(Double.parseDouble(xCenterField.getText()));
            int yc = (int) Math.round(Double.parseDouble(yCenterField.getText()));

            int rn = (int) Math.round(Double.parseDouble(rStartField.getText()));
            int dr = (int) Math.round(Double.parseDouble(rDeltaField.getText()));

            var center = new Point(xc, yc);
            var algo = circFuncs.get(algChoiceBox.getValue());
            var color = colors.get(colorChoiceBox.getValue());
            for (int i = 0; i < quantity; i++) {
                algo.accept(center, rn, color);

                rn += dr;
            }
        } catch (NumberFormatException e) {
            formErrorMessage(e.getMessage());
        }
    }

    @FXML
    protected void onEllipseSpectreButtonClicked() {
        if (quantityField.getText().isEmpty() || aStartField.getText().isEmpty() || bStartField.getText().isEmpty() ||
                aDeltaField.getText().isEmpty() || bDeltaField.getText().isEmpty() || xCenterField.getText().
            isEmpty() || yCenterField.getText().isEmpty()) {

            formErrorMessage("Поля ввода не могут быть пустыми");

            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());

            int xc = (int) Math.round(Double.parseDouble(xCenterField.getText()));
            int yc = (int) Math.round(Double.parseDouble(yCenterField.getText()));

            int an = (int) Math.round(Double.parseDouble(aStartField.getText()));
            int bn = (int) Math.round(Double.parseDouble(bStartField.getText()));
            int da = (int) Math.round(Double.parseDouble(aDeltaField.getText()));
            int db = (int) Math.round(Double.parseDouble(bDeltaField.getText()));

            var center = new Point(xc, yc);
            var algo = ellFuncs.get(algChoiceBox.getValue());
            var color = colors.get(colorChoiceBox.getValue());
            for (int i = 0; i < quantity; i++) {
                algo.accept(center, an, bn, color);

                an += da;
                bn += db;
            }
        } catch (NumberFormatException e) {
            formErrorMessage(e.getMessage());
        }
    }

    @FXML
    protected void onCleanButtonClicked() {
        tabPane.getSelectionModel().select(canvasTab);

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
                "каноническое уравнение",
                "параметрическое уравнение",
                "библиотечный алгоритм",
                "алгоритм Брезенхема",
                "алгоритм средней точки"
        ));
        algChoiceBox.applyCss();
        algChoiceBox.setValue("каноническое уравнение");

        colorChoiceBox.setItems(FXCollections.observableArrayList(
                "Черный", "Синий", "Красный", "Зеленый", "Цвет фона"
        ));
        colorChoiceBox.applyCss();
        colorChoiceBox.setValue("Черный");

        circFuncs = Map.of(
                "каноническое уравнение", renderer::canonCircle,
                "параметрическое уравнение", renderer::paramCircle,
                "библиотечный алгоритм", this::libCircle,
                "алгоритм Брезенхема", renderer::BresenhamCircle,
                "алгоритм средней точки", renderer::midPointCircle
        );

        ellFuncs = Map.of(
                "каноническое уравнение", renderer::canonEllipse,
                "параметрическое уравнение", renderer::paramEllipse,
                "библиотечный алгоритм", this::libEllipse,
                "алгоритм Брезенхема", renderer::BresenhamEllipse,
                "алгоритм средней точки", renderer::midPointEllipse
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

    private void libCircle(Point center, int radius, Color color) {
        var gc = canvas.getGraphicsContext2D();

        gc.setLineWidth(1);
        gc.setStroke(color);
        double newX = center.getX() - radius;
        double newY = center.getY() - radius;
        gc.strokeOval(newX + 0.5, newY + 0.5, 2 * radius, 2 * radius);
    }

    private void libEllipse(Point center, int a, int b, Color color) {
        var gc = canvas.getGraphicsContext2D();

        gc.setLineWidth(0.5);
        gc.setStroke(color);
        double newX = center.getX() - a;
        double newY = center.getY() - b;
        gc.strokeOval(newX + 0.5, newY + 0.5, 2 * a, 2 * b);
    }

    private Renderer renderer;
    private Map<String, TriConsumer<Point, Integer, Color>> circFuncs;
    private Map<String, ThriConsumer<Point, Integer, Integer, Color>> ellFuncs;
    private Map<String, Color> colors;
}