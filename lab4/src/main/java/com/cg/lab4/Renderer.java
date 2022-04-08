package com.cg.lab4;

import javafx.geometry.Point2D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Renderer {

    private final PixelWriter pixelWriter;
    public static final Color backgroundColor = Color.rgb(244, 244, 244);

    public Renderer(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    private void xySymmetryDraw(Point center, int x, int y, Color color) {
        pixelWriter.setColor(center.getX() + x, center.getY() + y, color);
        pixelWriter.setColor(center.getX() - x, center.getY() - y, color);
        pixelWriter.setColor(center.getX() + x, center.getY() - y, color);
        pixelWriter.setColor(center.getX() - x, center.getY() + y, color);
    }

    public void canonCircle(Point center, int radius, Color color) {
        int x, y;
        double finalX = radius / Math.sqrt(2);
        int rr = radius * radius;

        for (x = 0; x <= finalX; x++) {
            y = (int) Math.round(Math.sqrt(rr - x * x));

            xySymmetryDraw(center, x, y, color);
            xySymmetryDraw(center, y, x, color);
        }
    }

    public void canonEllipse(Point center, int a, int b, Color color) {
        int aa = a * a;
        int bb = b * b;

        int limit = (int) Math.round(aa / Math.sqrt(aa + bb));

        int x, y;
        double m = (double) b / a;

        for (x = 0; x <= limit; x++) {
            y = (int) Math.round(Math.sqrt(aa - x * x) * m);

            xySymmetryDraw(center, x, y, color);
        }

        limit = (int) Math.round(bb / Math.sqrt(aa + bb));
        m = 1 / m;

        for (y = 0; y <= limit; y++) {
            x = (int) Math.round(Math.sqrt(bb - y * y) * m);

            xySymmetryDraw(center, x, y, color);
        }
    }

    public void paramCircle(Point center, int radius, Color color) {
        double t, step = 1 / (double) radius;
        int x, y;

        for (t = 0; t < Math.PI / 4; t += step) {
            x = (int) Math.round(radius * Math.cos(t));
            y = (int) Math.round(radius * Math.sin(t));

            xySymmetryDraw(center, x, y, color);
            xySymmetryDraw(center, y, x, color);
        }
    }

    public void paramEllipse(Point center, int a, int b, Color color) {
        double tau = Math.PI / 2;

        double dt = 1 / (double) Math.max(a, b);

        for (double t = 0.0; t < tau; t += dt) {
            int x = (int) Math.round(a * Math.cos(t));
            int y = (int) Math.round(b * Math.sin(t));

            xySymmetryDraw(center, x, y, color);
        }
    }

    public void BresenhamCircle(Point center, int radius, Color color) {
        int x = 0, y = radius;
        int d = 2 * (1 - radius), yk = 0;
        int d1, d2;

        double halfR = Math.ceil(radius / Math.sqrt(2));

        while (x <= halfR) {
            xySymmetryDraw(center, x, y, color);
            xySymmetryDraw(center, y, x, color);

            if (d == 0) { // диагональная точка на окружности
                x++;
                y--;
                d += 2 * (x - y + 1);
            } else if (d < 0) { // диагональная точка внутри окружности
                d1 = 2 * d + 2 * y - 1;
                if (d1 > 0) {
                    x++;
                    y--;
                    d += 2 * (x - y + 1);
                } else {
                    x++;
                    d += 2 * x + 1;
                }
            } else { // диагональная точка внутри окружности
                d2 = 2 * d - 2 * x - 1;
                if (d2 < 0) {
                    x++;
                    y--;
                    d += 2 * (x - y + 1);
                } else {
                    y--;
                    d += -2 * y + 1;
                }
            }
        }
    }

    public void BresenhamEllipse(Point center, int a, int b, Color color) {
        int aa = a * a;
        int bb = b * b;
        int bb2 = 2 * bb;
        int aa2 = 2 * aa;
        int x = 0, y = b;

        int d = aa + bb - aa2 * y;
        int d1, d2;

        while (y >= 0) {
            xySymmetryDraw(center, x, y, color);

            if (d == 0) {
                x++;
                y--;
                d += bb2 * x + bb + aa - aa2 * y;
            } else if (d < 0) {
                d1 = 2 * d + y * aa2 - aa;
                if (d1 > 0) {
                    x++;
                    y--;
                    d += bb2 * x + bb + aa - aa2 * y;
                } else {
                    x++;
                    d += bb2 * x + bb;
                }
            } else {
                d2 = 2 * d - bb2 * x - bb;
                if (d2 < 0) {
                    x++;
                    y--;
                    d += bb2 * x + bb + aa - aa2 * y;
                } else {
                    y--;
                    d += aa - aa2 * y;
                }
            }
        }
    }

    public void midPointCircle(Point center, int radius, Color color) {
        int x = 0, y = radius;

        int f = (int) Math.round(1.25 - radius);

        while (y >= x) {
            xySymmetryDraw(center, x, y, color);
            xySymmetryDraw(center, y, x, color);

            if (f < 0) {
                f += 2 * ++x + 1;
            } else {
                f += 2 * ++x + 1 - 2 * --y;
            }
        }
    }

    public void midPointEllipse(Point center, int a, int b, Color color) {
        int aa = a * a;
        int bb = b * b;
        int aa2 = 2 * aa;
        int bb2 = 2 * bb;

        int limit = (int) (aa / Math.sqrt(aa + bb));

        int x, y = b;

        int df = 0;
        int f = (int) Math.round(bb - aa * b + 0.25 * aa);

        int delta = -aa2 * y;
        for (x = 0; x <= limit; x++) {
            xySymmetryDraw(center, x, y, color);

            if (f > 0) {
                y -= 1;
                delta += aa2;
                f += delta;
            }
            df += bb2;
            f += df + bb;
        }

        delta = bb2 * x;
        f += (int) (-bb * (x + 0.75) - aa * (y - 0.75));
        df = -aa2 * y;

        for (; y >= 0; y--) {
            xySymmetryDraw(center, x, y, color);

            if (f < 0) {
                x += 1;
                delta += bb2;
                f += delta;
            }
            df += aa2;
            f += df + aa;
        }
    }
}
