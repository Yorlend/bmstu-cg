package com.cg.lab3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Renderer renderer = new Renderer(canvas.getGraphicsContext2D().getPixelWriter());

        int cx = (int) (canvas.getWidth() / 2), cy = (int) (canvas.getHeight() / 2);
        int radius = 255;
        int quantity = 100;

        for (int i = 0; i < quantity; i++) {
            double angle = 2 * Math.PI * i / quantity;
            int xk = (int) (cx + radius * Math.cos(angle));
            int yk = (int) (cy + radius * Math.sin(angle));

            renderer.DDA(new Point(cx, cy), new Point(xk, yk), Color.BLACK);
        }

        for (int i = 0; i < quantity; i++) {
            double angle = 2 * Math.PI * i / quantity;
            int xk = (int) (cx + radius * Math.cos(angle));
            int yk = (int) (cy + radius * Math.sin(angle));

            renderer.BresenhamFloat(new Point(cx, cy), new Point(xk, yk), Color.WHITE);
        }
    }
}