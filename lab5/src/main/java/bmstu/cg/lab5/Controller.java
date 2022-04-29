package bmstu.cg.lab5;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Label timeLabel;

    @FXML
    private TextField xField, yField;

    @FXML
    private TableView<PointModel> table;

    @FXML
    private TableColumn<PointModel, Integer> xCol, yCol;

    private void formError(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Некорректный ввод");
        errorAlert.setContentText(text);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.showAndWait();
    }

    private void formInfo(String text) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText("Внимание");
        info.setContentText(text);
        info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        info.showAndWait();
    }

    @FXML
    protected void onInputPoint() {
        try {

            int x = (int) Double.parseDouble(xField.getText());
            int y = (int) Double.parseDouble(yField.getText());

            var point = new PointModel(x, y);

            if (points.contains(point)) {
                formInfo("Введена существующая точка");
            } else {
                points.add(new PointModel(x, y));
                polygonBuilder.appendPoint(x, y);

                clearCanvas();
                renderPolyline(polygonBuilder.getResult());
            }
        } catch (Exception e) {
            formError(e.getMessage());
        }
    }

    @FXML
    protected void onClosePath() {
        polygonBuilder.closePolygon();
        clearCanvas();
        renderPolyline(polygonBuilder.getResult());
    }

    @FXML
    protected void onMousePressed(MouseEvent event) {
        int x = (int) event.getX(), y = (int) event.getY();
        if (event.isShiftDown()) {
            y = polygonBuilder.appendHorizontalPoint(x, y);
        } else if (event.isControlDown()) {
            x = polygonBuilder.appendVerticalPoint(x, y);
        } else {
            polygonBuilder.appendPoint(x, y);
        }

        points.add(new PointModel(x, y));

        // render lines
        clearCanvas();
        renderPolyline(polygonBuilder.getResult());
    }

    @FXML
    protected void onFill() {
        timeLabel.setText(filler.fill(polygonBuilder.getResult(), colorPicker.getValue()));
    }

    @FXML
    protected void onStepFill() {
        var stepFiller = new StepFiller(canvas);
        stepFiller.startFill(polygonBuilder.getResult(), colorPicker.getValue());

        var timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (stepFiller.hasNext()) {
                    stepFiller.stepFill();
                } else {
                    stop();
                }
            }
        };

        timer.start();
    }

    @FXML
    protected void onClear() {
        clearCanvas();
        polygonBuilder = new PolygonBuilder();
        points.clear();
    }

    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.C) {
            polygonBuilder.closePolygon();
            clearCanvas();
            renderPolyline(polygonBuilder.getResult());
        } else if (event.getCode() == KeyCode.F) {
            timeLabel.setText(filler.fill(polygonBuilder.getResult(), colorPicker.getValue()));
        } else if (event.getCode() == KeyCode.Q) {
            clearCanvas();
            polygonBuilder = new PolygonBuilder();
            points.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        xCol.setCellValueFactory(new PropertyValueFactory<>("X"));
        yCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yCol.setCellValueFactory(new PropertyValueFactory<>("Y"));

        filler = new Filler(canvas);
        outliner = new Outliner(canvas.getGraphicsContext2D().getPixelWriter());

        table.setItems(points);
    }

    private void clearCanvas() {
        var ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        timeLabel.setText("");
    }

    private void renderPolyline(Polygon polygon) {
        for (var line : polygon.getEdges()) {
            outliner.BresenhamInt(line.getStart(), line.getEnd(), colorPicker.getValue());
        }
    }

    private PolygonBuilder polygonBuilder = new PolygonBuilder();

    private Outliner outliner;
    private Filler filler;

    private final ObservableList<PointModel> points = FXCollections.observableArrayList();
}