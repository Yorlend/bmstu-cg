package bmstu.cg.lab8;

import javafx.collections.ObservableList;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LineRenderer {

    private final PixelWriter pixelWriter;

    private Point start;
    private final List<Line> lines = new ArrayList<>();

    public LineRenderer(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void appendPoint(ObservableList<LineModel> models, int x, int y, Color color) {
        if (start == null) {
            start = new Point(x, y);
        } else {
            var line = new Line(start, new Point(x, y));
            renderLine(line, color);
            lines.add(line);
            models.add(new LineModel(line));
            start = null;
        }
    }

    public int appendHorizontalPoint(ObservableList<LineModel> models, int x, int y, Color color) {
        if (start == null) {
            start = new Point(x, y);
            return y;
        }

        y = start.getY();
        var line = new Line(start, new Point(x, y));
        renderLine(line, color);
        lines.add(line);
        models.add(new LineModel(line));
        start = null;

        return y;
    }

    public int appendVerticalPoint(ObservableList<LineModel> models, int x, int y, Color color) {
        if (start == null) {
            start = new Point(x, y);
            return x;
        }

        x = start.getX();
        var line = new Line(start, new Point(x, y));
        renderLine(line, color);
        lines.add(line);
        models.add(new LineModel(line));
        start = null;

        return x;
    }

    public void renderLine(Line line, Color color) {
        var start = line.getStart();
        var end = line.getEnd();
        int dx = end.getX() - start.getX(), dy = end.getY() - start.getY();
        int sx = Integer.signum(dx), sy = Integer.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        boolean swap = dy > dx;
        if (swap) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
        }
        int e = 2 * dy - dx;

        int x = start.getX(), y = start.getY();
        for (int i = 0; i < dx + 1; i++) {
            pixelWriter.setColor(x, y, color);
            if (e >= 0) {
                if (swap) {
                    x += sx;
                } else {
                    y += sy;
                }
                e -= 2 * dx ;
            }
            if (swap) {
                y += sy;
            } else {
                x += sx;
            }

            e += 2 * dy;
        }
    }

    private Color getIntensiveColor(Color color, double e, int I) {

        double r = color.getRed() * 255;
        double g = color.getGreen() * 255;
        double b = color.getBlue() * 255;

        double rb = 244, gb = 244, bb = 244;

        double t = Math.floor(e) / I;

        r += (rb - r) * t;
        g += (gb - g) * t;
        b += (bb - b) * t;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public void Wu(Line line, Color color) {

        var start = line.getStart();
        var end = line.getEnd();

        int I = 255;

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        int sx = (int) Math.signum(dx);
        int sy = (int) Math.signum(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        // отрисовка горизонтальных и вертикальных прямых
        if (dx == 0) {
            int x = start.getX();
            int y = start.getY();
            for (int i = 0; i < dy + 1; i++) {
                pixelWriter.setColor(x, y, color);
                y += sy;
            }
        } else if (dy == 0) {
            int x = start.getX();
            int y = start.getY();
            for (int i = 0; i < dx + 1; i++) {
                pixelWriter.setColor(x, y, color);
                x += sx;
            }
        }

        boolean swap = dy > dx;

        if (swap) {
            double tmp = dx;
            dx = dy;
            dy = tmp;

            int stmp = sx;
            sx = sy;
            sy = stmp;
        }

        double m = dy / dx;

        int X = swap ? start.getY() : start.getX();
        double Y = swap ? start.getX() : start.getY();

        for (int i = 0; i < dx + 1; i++) {
            double r1 = I * fracPart(Y);
            double r2 = I * revFracPart(Y);

            int Yr = (int) Math.floor(Y);

            if (swap) {
                pixelWriter.setColor(Yr, X, getIntensiveColor(color, r1, I));
                pixelWriter.setColor(Yr + 1, X, getIntensiveColor(color, r2, I));
            } else {
                pixelWriter.setColor(X, Yr, getIntensiveColor(color, r1, I));
                pixelWriter.setColor(X, Yr + 1, getIntensiveColor(color, r2, I));
            }

            X += sx;
            Y += sy * m;
        }
    }

    private double fracPart(double num) {
        return num - (long) num;
    }

    private double revFracPart(double num) {
        return 1 - fracPart(num);
    }

    public void renderLine(int xs, int ys, int xe, int ye, Color color) {
        renderLine(new Line(xs, ys, xe, ye), color);
    }

    public void renderLine(Point p1, Point p2, Color color) {
        renderLine(new Line(p1, p2), color);
    }

    public boolean nextLine() {
        return start != null;
    }

    public Point getStart() {
        return start;
    }

    public void resetStart() {
        start = null;
    }
}
