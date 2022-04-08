package com.cg.lab4;

import javafx.scene.paint.Color;

import java.util.Map;

public class AlgTimer {

    private static final long ITERS = 5000;

    public static Map<String, TriFunction<Point, Integer, Color, Long>> circMap = Map.of(
                "каноническое уравнение", AlgTimer::canonCircle,
                "параметрическое уравнение", AlgTimer::paramCircle,
                "алгоритм Брезенхема", AlgTimer::BresenhamCircle,
                "алгоритм средней точки", AlgTimer::midPointCircle
            );

    public static Map<String, ThriFunction<Point, Integer, Integer, Color, Long>> ellMap = Map.of(
                "каноническое уравнение", AlgTimer::canonEllipse,
                "параметрическое уравнение", AlgTimer::paramEllipse,
                "алгоритм Брезенхема", AlgTimer::BresenhamEllipse,
                "алгоритм средней точки", AlgTimer::midPointEllipse
    );

    public static long canonCircle(Point center, int radius, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            int x, y;
            double finalX = radius / Math.sqrt(2);
            int rr = radius * radius;

            for (x = 0; x <= finalX; x++) {
                y = (int) Math.round(Math.sqrt(rr - x * x));
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long canonEllipse(Point center, int a, int b, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            int aa = a * a;
            int bb = b * b;

            int limit = (int) Math.round(aa / Math.sqrt(aa + bb));

            int x, y;
            double m = (double) b / a;

            for (x = 0; x <= limit; x++) {
                y = (int) Math.round(Math.sqrt(aa - x * x) * m);
            }

            limit = (int) Math.round(bb / Math.sqrt(aa + bb));
            m = 1 / m;

            for (y = 0; y <= limit; y++) {
                x = (int) Math.round(Math.sqrt(bb - y * y) * m);
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long paramCircle(Point center, int radius, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            double t, step = 1 / (double) radius;
            int x, y;

            for (t = 0; t < Math.PI / 4; t += step) {
                x = (int) Math.round(radius * Math.cos(t));
                y = (int) Math.round(radius * Math.sin(t));
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long paramEllipse(Point center, int a, int b, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            double tau = Math.PI / 2;

            double dt = 1 / (double) Math.max(a, b);

            for (double t = 0.0; t < tau; t += dt) {
                int x = (int) Math.round(a * Math.cos(t));
                int y = (int) Math.round(b * Math.sin(t));
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long BresenhamCircle(Point center, int radius, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            int x = 0, y = radius;
            int d = 2 * (1 - radius), yk = 0;
            int d1, d2;

            double halfR = Math.ceil(radius / 2);

            while (x <= halfR) {
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

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long BresenhamEllipse(Point center, int a, int b, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            int aa = a * a;
            int bb = b * b;
            int bb2 = 2 * bb;
            int aa2 = 2 * aa;
            int x = 0, y = b;

            int d = aa + bb - aa2 * y;
            int d1, d2;

            while (y >= 0) {
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

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long midPointCircle(Point center, int radius, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
            int x = 0, y = radius;

            int f = (int) Math.round(1.25 - radius);

            while (y >= x) {
                if (f < 0) {
                    f += 2 * ++x + 1;
                } else {
                    f += 2 * ++x + 1 - 2 * --y;
                }
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }

    public static long midPointEllipse(Point center, int a, int b, Color color) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERS; i++)
        {
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
                if (f < 0) {
                    x += 1;
                    delta += bb2;
                    f += delta;
                }
                df += aa2;
                f += df + aa;
            }
        }

        return (System.nanoTime() - startTime) / ITERS;
    }
}
