package bmstu.cg.lab7;

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
    private TableView<LineModel> table;

    @FXML
    private ColorPicker colorPicker, cutterColorPicker, boldColorPicker;

    @FXML
    private TableColumn<LineModel, Integer> xStartCol, yStartCol, xEndCol, yEndCol;

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
            var res = SutherlandCohen.cutLine(line, cutter);

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
    }

    @FXML
    protected void onKeyPressed() {

    }

    @FXML
    protected void onMousePressed(MouseEvent event) {

        int x = (int) event.getX(), y = (int) event.getY();

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

        lineRenderer = new LineRenderer(canvas.getGraphicsContext2D().getPixelWriter());

        cutter = new Cutter(
                (int)((canvas.getWidth() - RECT_WIDTH) / 2),
                (int)((canvas.getWidth() + RECT_WIDTH) / 2),
                (int)((canvas.getHeight() + RECT_HEIGHT) / 2),
                (int)((canvas.getHeight() - RECT_HEIGHT) / 2)
        );
        renderRect();

        table.setItems(lines);
    }

    private void renderRect() {
        int x1 = (int)((canvas.getWidth() - RECT_WIDTH) / 2);
        int y1 = (int)((canvas.getHeight() - RECT_HEIGHT) / 2);
        int x2 = x1 + RECT_WIDTH;
        int y2 = y1 + RECT_HEIGHT;

        var p1 = new Point(x1, y1);
        var p2 = new Point(x2, y1);
        var p3 = new Point(x1, y2);
        var p4 = new Point(x2, y2);

        var color = Color.ORANGERED;
        lineRenderer.renderLine(p1, p2, color);
        lineRenderer.renderLine(p1, p3, color);
        lineRenderer.renderLine(p3, p4, color);
        lineRenderer.renderLine(p2, p4, color);
    }

    private void clearCanvas() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        lines.clear();
        lineRenderer.clearLines();
        renderRect();
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
    private final int RECT_WIDTH = 300;
    private final int RECT_HEIGHT = 150;
}