package bmstu.cg.lab5;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
    protected void onMousePressed(MouseEvent event) {
        if (event.isShiftDown()) {
            polygonBuilder.appendHorizontalPoint((int)event.getX(), (int)event.getY());
        } else if (event.isControlDown()) {
            polygonBuilder.appendVerticalPoint((int)event.getX(), (int)event.getY());
        } else {
            polygonBuilder.appendPoint((int)event.getX(), (int)event.getY());
        }

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
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filler = new Filler(canvas);
        outliner = new Outliner(canvas.getGraphicsContext2D().getPixelWriter());
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
}