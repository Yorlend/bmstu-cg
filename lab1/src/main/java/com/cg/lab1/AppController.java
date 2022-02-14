package com.cg.lab1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable {
    private static final double POINT_RADIUS = 2;
    private static final double PADDING = 20;

    @FXML
    private TextField xCoord;
    @FXML
    private TextField yCoord;

    @FXML
    private Canvas canvas;

    @FXML
    private TextArea answer;

    @FXML
    private TableView<PointModel> table;

    @FXML
    private TableColumn<PointModel, Double> xColumn;

    @FXML
    private TableColumn<PointModel, Double> yColumn;

    @FXML
    private Label inputLabel;

    @FXML
    protected void onInputButtonClick() throws NumberFormatException {
        if (xCoord.getText().isEmpty() || yCoord.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Некорректный ввод");
            errorAlert.setContentText("Значения координат не должны быть пустыми");
            errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            errorAlert.showAndWait();
            return;
        }

        try {
            /// TODO: округлять штуки

            Double x = Double.valueOf(xCoord.getText());
            Double y = Double.valueOf(yCoord.getText());

            var point = new PointModel(x, y);

            if (pointModels.contains(point)) {
                inputLabel.setText("Введена существующая точка.");
            } else {
                pointModels.add(new PointModel(x, y));
                inputLabel.setText(String.format("Введенная точка: (%.2f, %.2f)", x, y));
            }
        } catch (NumberFormatException e) {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Некорректный ввод");
            errorAlert.setContentText("Значения координат содержит недопустимые символы");
            errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            errorAlert.showAndWait();
        }
    }

    @FXML
    protected void onSolveButtonClick() {
        var result = Solution.solve(extractPoints2D());

        clearCanvas();
        if (result == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Вырожденный случай");
            error.setContentText("Решение не может быть получено: нельзя построить треугольник");
            error.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            error.showAndWait();
        } else {
            answer.setText(String.format("Искомый треугольник построен " +
                    "на точках:\n%s", result));
            drawSolution(result);
        }
    }

    @FXML
    protected void onRemoveButtonClick() {
        PointModel selected = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selected);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        xColumn.setOnEditCommit(event -> {
            try {
                PointModel point = event.getRowValue();
                event.getOldValue();
                point.setX(event.getNewValue());
            } catch (NullPointerException e) {
            }
        });

        yColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yColumn.setOnEditCommit(event -> {
            try {
                PointModel point = event.getRowValue();
                point.setY(event.getNewValue());
            } catch (NullPointerException e) {
            }
        });

        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));

        table.setItems(pointModels);

    }

    private void drawSolution(Triangle triangle) {
        triangle = mapTriangle(triangle);
        var points = triangle.getPoints();
        var bisects = triangle.getBisects();

        var context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setFill(Color.BLACK);

        for (var p : points) {
            context.fillOval(p.getX() - POINT_RADIUS, p.getY() - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        context.beginPath();
        context.lineTo(points.get(0).getX(), points.get(0).getY());
        context.lineTo(points.get(1).getX(), points.get(1).getY());
        context.lineTo(points.get(2).getX(), points.get(2).getY());
        context.closePath();
        context.stroke();

        context.setStroke(Color.HOTPINK);
        context.beginPath();
        // context.moveTo(points.get(0).getX(), points.get(0).getY());
        // context.lineTo(triangle.);

        for (var bisect : bisects) {
            context.moveTo(bisect.get(0).getX(), bisect.get(0).getY());
            context.lineTo(bisect.get(1).getX(), bisect.get(1).getY());
        }

        context.stroke();
    }

    private void clearCanvas() {
        var context = canvas.getGraphicsContext2D();

        context.setFill(Color.WHITE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private Point2D mapPoint(Point2D point, Bounds inputBounds, Bounds outputBounds) {
        double xm = outputBounds.getCenterX();
        double ym = outputBounds.getCenterY();

        double xc = inputBounds.getCenterX();
        double yc = inputBounds.getCenterY();

        double kx = outputBounds.getWidth() / inputBounds.getWidth();
        double ky = outputBounds.getHeight() / inputBounds.getHeight();

        double k = Math.min(kx, ky);

        double newX = xm + (point.getX() - xc) * k;
        double nexY = ym - (point.getY() - yc) * k;

        return new Point2D(newX, nexY);
    }

    private Triangle mapTriangle(Triangle triangle) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (var p : triangle.getPoints()) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        Bounds inputBounds = new BoundingBox(minX, minY, maxX - minX, maxY - minY);
        Bounds outputBounds = getCanvasBounds();

        return new Triangle(
                mapPoint(triangle.getFirstPoint(), inputBounds, outputBounds),
                mapPoint(triangle.getSecondPoint(), inputBounds, outputBounds),
                mapPoint(triangle.getThirdPoint(), inputBounds, outputBounds)
        );
    }

    private Bounds getCanvasBounds() {
        Bounds bounds = canvas.getBoundsInLocal();

        double minX = bounds.getMinX() + PADDING;
        double minY = bounds.getMinY() + PADDING;
        double width = bounds.getWidth() - 2 * PADDING;
        double height = bounds.getHeight() - 2 * PADDING;

        return new BoundingBox(minX, minY, width, height);
    }

    private List<Point2D> extractPoints2D() {
        return pointModels.stream()
                .map(p -> new Point2D(p.getX(), p.getY()))
                .collect(Collectors.toList());
    }

    private final ObservableList<PointModel> pointModels = FXCollections.observableArrayList(
            new PointModel(10, 11),
            new PointModel(1, 4),
            new PointModel(0, 7),
            new PointModel(3, 2),
            new PointModel(6, 6),
            new PointModel(7, 9)
    );
}