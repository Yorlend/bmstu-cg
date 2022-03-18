package com.cg.lab3;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Renderer {

    private final PixelWriter pixelWriter;

    public Renderer(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public void DDA(Point start, Point end, Color color) {
        /// Potential warning! int dx?
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double delta = Math.max(Math.abs(dx), Math.abs(dy));

        dx /= delta;
        dy /= delta;

        double x = start.getX(), y = start.getY();
        for (int i = 0; i < delta + 1; i++) {
            pixelWriter.setColor((int) Math.round(x), (int) Math.round(y), color);
            x += dx;
            y += dy;
        }
    }

    public void BresenhamFloat(Point start, Point end, Color color) {

        double dx = end.getX() - start.getX(), dy = end.getY() - start.getY();
        int sx = (int) Math.signum(dx), sy = (int) Math.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        boolean swap = dy > dx;
        double m = swap ? dx / dy : dy / dx;
        double e = m - 0.5;

        double limit = swap ? dy + 1 : dx + 1;
        int x = start.getX(), y = start.getY();
        for (int i = 0; i < limit; i++) {
            pixelWriter.setColor(x, y, color);
            if (e >= 0) {
                if (swap) {
                    x += sx;
                } else {
                    y += sy;
                }
                e -= 1;
            }
            if (swap) {
                y += sy;
            } else {
                x += sx;
            }

            e += m;
        }
    }
}
