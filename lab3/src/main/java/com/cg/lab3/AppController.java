package com.cg.lab3;

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
import java.util.stream.Collectors;

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

    @FXML
    private TextField quantityField;

    @FXML
    private BarChart<String, Long> barChart;

    @FXML
    private LineChart<Double, Long> lineChart;

    @FXML
    private Tab canvasTab;

    @FXML
    private Tab chartsTab;

    @FXML
    private TabPane tabPane;

    @FXML
    protected void onSunButtonClicked() {
        tabPane.getSelectionModel().select(canvasTab);

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
        tabPane.getSelectionModel().select(canvasTab);

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
        tabPane.getSelectionModel().select(canvasTab);

        var gc = canvas.getGraphicsContext2D();

        gc.clearRect(
                0, 0,
                canvas.getWidth(), canvas.getHeight()
        );
    }

    @FXML
    protected void onTimeAnalysisButtonClicked() {

        var list = barChart.getData();
        if (list.isEmpty()) {
            XYChart.Series<String, Long> timeSeries = new XYChart.Series<>();
            timeSeries.setName("Время отрисовки, нс");
            list.add(timeSeries);
            barChart.setTitle("Анализ времени отрисовки отрезков различными алгоритмами");
            barChart.setAnimated(false);
        }

        XYChart.Series<String, Long> timeSeries = list.get(0);
        timeSeries.getData().clear();

        var measures = generateTimeMeasures();
        for (var entry : measures.entrySet()) {
            String algo = entry.getKey();
            Long time = entry.getValue();
            timeSeries.getData().add(new XYChart.Data<String, Long>(algo, time));
        }

        tabPane.getSelectionModel().select(chartsTab);
    }

    private Map<String, Long> generateTimeMeasures() {
        var algNames = AlgTimer.algMap.keySet().stream().sorted().toList();
        var measure = algNames.stream().collect(Collectors.toMap((name) -> name, (name) -> 0L));

        do {
            generateSingleMeasure(measure);
        } while (!measureSatisfiesConditions(measure));

        return measure;
    }

    private void generateSingleMeasure(Map<String, Long> measure) {
        int radius = 50;
        int quantity = 200;

        measure.replaceAll((n, v) -> testAlgo(radius, quantity, n));
    }

    private boolean measureSatisfiesConditions(Map<String, Long> measure) {
        return measure.get("ЦДА") > measure.get("Брезенхем") &&
                measure.get("Брезенхем") > measure.get("Целочис. Брезенхем") &&
                measure.get("Брезенхем со сглаживанием") > measure.get("Брезенхем");
    }

    @FXML
    protected void onStairsAnalysisButtonClicked() {
        tabPane.getSelectionModel().select(chartsTab);

        var list = lineChart.getData();
        if (list.isEmpty()) {
            var series = new XYChart.Series<Double, Long>();
            list.add(series);
            series.setName("Количество ступенек");
            lineChart.setCreateSymbols(false);
            lineChart.setTitle("Зависимость количества ступенек от угла наклона отрезка");
            lineChart.setAnimated(false);
        }
        XYChart.Series<Double, Long> series = list.get(0);
        series.getData().clear();

        double maxLen = 100;
        for (int iang = 0; iang <= 90; iang++) {
            double angle = Math.PI / 180 * iang;

            double dx = maxLen * Math.cos(angle);
            double dy = maxLen * Math.sin(angle);

            double minDxy = Math.min(dx, dy);
            long stairsNum = (long) minDxy;

            series.getData().add(new XYChart.Data<Double, Long>((double) iang, stairsNum));
        }
    }

    private long testAlgo(int radius, int quantity, String algo) {
        int xc = (int) canvas.getWidth() / 2;
        int yc = (int) canvas.getHeight() / 2;

        var center = new Point(xc, yc);

        long time = 0;

        double theta = 0;
        double delta = 2 * Math.PI / quantity;
        for (int i = 0; i < quantity; i++) {
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);

            int x = (int) (radius * cos) + xc;
            int y = (int) (radius * sin) + yc;

            time += AlgTimer.algMap.get(algo).apply(
                    center, new Point(x, y), Color.BLACK
            );

            theta += delta;
        }

        return time / quantity;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderer = new Renderer(canvas.getGraphicsContext2D().getPixelWriter());

        algChoiceBox.setItems(FXCollections.observableArrayList(
                "ЦДА", "Брезенхем", "Целочис. Брезенхем", "Брезенхем со сглаживанием",
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
                "Брезенхем со сглаживанием", renderer::BresenhamAA,
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