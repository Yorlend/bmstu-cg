package bmstu.cg.lab8;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField xStart, yStart, xEnd, yEnd;

    @FXML
    private CheckBox paraSelection;

    @FXML
    private TableView<LineModel> table, cutterTable;

    @FXML
    private ColorPicker colorPicker, cutterColorPicker, boldColorPicker;

    @FXML
    private TableColumn<LineModel, Integer> xStartCol, yStartCol, xEndCol, yEndCol,
            cutXStart, cutYStart, cutXEnd, cutYEnd;

    @FXML
    private RadioButton cutterRadio, lineRadio;

    @FXML
    protected void onInputLine() {
        try {

            int xs = (int) Double.parseDouble(xStart.getText());
            int ys = (int) Double.parseDouble(yStart.getText());
            int xe = (int) Double.parseDouble(xEnd.getText());
            int ye = (int) Double.parseDouble(yEnd.getText());

            var line = new LineModel(xs, ys, xe, ye);

            if (lines.contains(line)) {
                formInfo("Введена существующая линия");
            } else {
                lineRenderer.renderLine(new Line(xs, ys, xe, ye), colorPicker.getValue());
                lines.add(line);
            }
        } catch (Exception e) {
            formError(e.getMessage());
        }
    }

    @FXML
    protected void onCutLines() {
        var lineList = lineRenderer.getLines();

        for (var line : lineList) {
            var res = CyrusBeck.cutLine(line, cutter);

            if (res != null) {
                lineRenderer.Wu(line, boldColorPicker.getValue());
//                var gc = canvas.getGraphicsContext2D();
//
//                gc.beginPath();
//
//                gc.setLineWidth(3);
//                gc.setStroke(boldColorPicker.getValue());
//
//                gc.moveTo(line.getStart().getX() + 0.5, line.getStart().getY() + 0.5);
//                gc.lineTo(line.getEnd().getX() + 0.5, line.getEnd().getY() + 0.5);
//
//                gc.stroke();
            }
        }
    }

    @FXML
    protected void onClearCanvas() {
        clearCanvas();
        lines.clear();
        cutterLines.clear();
        cutter.clear();
    }

    @FXML
    protected void onKeyPressed() {

    }

    @FXML
    protected void onClosePath() {
        cutter.close(cutterLines);

        var line = cutter.getLast();
        if (line != null)
            lineRenderer.renderLine(line, cutterColorPicker.getValue());

        cutter.clear();
    }

    @FXML
    protected void onMousePressed(MouseEvent event) {

        int x = (int) event.getX(), y = (int) event.getY();

        if (cutterRadio.isSelected()) {
            if (event.isShiftDown()) {
                y = cutter.appendHorizontalPoint(cutterLines, x, y);
            } else if (event.isControlDown()) {
                x = cutter.appendVerticalPoint(cutterLines, x, y);
            } else {
                cutter.appendPoint(cutterLines, x, y);
            }

            var line = cutter.getLast();
            if (line != null)
                lineRenderer.renderLine(line, cutterColorPicker.getValue());

            return;
        }

        var selected = cutterTable.getSelectionModel().getSelectedItem();
        if (selected != null && paraSelection.isSelected() && lineRenderer.nextLine()) {
            int qx = selected.getXs();
            int qy = selected.getYs();

            int px = selected.getXe();
            int py = selected.getYe();

            var p1 = new Point(qx, qy);
            var p2 = new Point(px, py);

            var start = lineRenderer.getStart();
            var end = new Point(x, y);

            var pDir = Vector.vectorize(start, end);

            var vec = Vector.direction(p1, p2);

            vec = vec.multiply(pDir.dot(vec));

            end = start.add(vec);

            lineRenderer.renderLine(start, end, colorPicker.getValue());

            lineRenderer.resetStart();

            return;
        }

        if (event.isShiftDown()) {
            y = lineRenderer.appendHorizontalPoint(lines, x, y, colorPicker.getValue());
        } else if (event.isControlDown()) {
            x = lineRenderer.appendVerticalPoint(lines, x, y, colorPicker.getValue());
        } else {
            lineRenderer.appendPoint(lines, x, y, colorPicker.getValue());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        xStartCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        xStartCol.setCellValueFactory(new PropertyValueFactory<>("xs"));
        yStartCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yStartCol.setCellValueFactory(new PropertyValueFactory<>("ys"));

        xEndCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        xEndCol.setCellValueFactory(new PropertyValueFactory<>("xe"));
        yEndCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yEndCol.setCellValueFactory(new PropertyValueFactory<>("ye"));

        cutXStart.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cutXStart.setCellValueFactory(new PropertyValueFactory<>("xs"));
        cutYStart.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cutYStart.setCellValueFactory(new PropertyValueFactory<>("ys"));

        cutXEnd.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cutXEnd.setCellValueFactory(new PropertyValueFactory<>("xe"));
        cutYEnd.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cutYEnd.setCellValueFactory(new PropertyValueFactory<>("ye"));

        lineRenderer = new LineRenderer(canvas.getGraphicsContext2D().getPixelWriter());

        cutter = new Cutter();

        table.setItems(lines);
        cutterTable.setItems(cutterLines);
    }

    private void clearCanvas() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        lines.clear();
        lineRenderer.clearLines();
    }

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

    private Cutter cutter;
    private LineRenderer lineRenderer;
    private final ObservableList<LineModel> lines = FXCollections.observableArrayList();
    private final ObservableList<LineModel> cutterLines = FXCollections.observableArrayList();
}