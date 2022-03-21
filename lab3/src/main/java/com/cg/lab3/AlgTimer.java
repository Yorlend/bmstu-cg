package com.cg.lab3;

import javafx.scene.paint.Color;

import java.util.Map;

public class AlgTimer {

    public static Map<String, TriFunction<Point, Point, Color, Long>> algMap = Map.of(
            "ЦДА", AlgTimer::DDA,
            "Брезенхем", AlgTimer::BresenhamFloat,
            "Целочис. Брезенхем", AlgTimer::BresenhamInt,
            "Брезенхем со сглаживанием", AlgTimer::BresenhamAA,
            "Ву", AlgTimer::Wu
            );
    public static long DDA(Point start, Point end, Color color) {

        long startTime = System.nanoTime();

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double delta = Math.max(Math.abs(dx), Math.abs(dy));

        dx /= delta;
        dy /= delta;

        double x = start.getX(), y = start.getY();
        for (int i = 0; i < delta + 1; i++) {
            int newX = (int) Math.round(x);
            int newY = (int) Math.round(y);
            x += dx;
            y += dy;
        }

        return System.nanoTime() - startTime;
    }

    public static long BresenhamFloat(Point start, Point end, Color color) {

        long startTime = System.nanoTime();

        double dx = end.getX() - start.getX(), dy = end.getY() - start.getY();
        int sx = (int) Math.signum(dx), sy = (int) Math.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        boolean swap = dy > dx;
        if (swap) {
            double tmp = dx;
            dx = dy;
            dy = tmp;
        }
        double m = dy / dx;
        double e = m - 0.5;

        int x = start.getX(), y = start.getY();
        for (int i = 0; i < dx + 1; i++) {
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

        return System.nanoTime() - startTime;
    }

    public static long BresenhamInt(Point start, Point end, Color color) {

        long startTime = System.nanoTime();

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

        return System.nanoTime() - startTime;
    }

    public static long BresenhamAA(Point start, Point end, Color color) {

        long startTime = System.nanoTime();

        int I = 100;

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        int sx = (int) Math.signum(dx);
        int sy = (int) Math.signum(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        boolean swap = dy > dx;
        if (swap) {
            double tmp = dx;
            dx = dy;
            dy = tmp;
        }
        double m = dy / dx;
        double e = I / 2.0;

        int x = start.getX();
        int y = start.getY();
        m *= I;
        double w = I - m;

        for (int i = 0; i < dx + 1; i++) {
            Color newColor = getIntensiveColor(color, e, I);

            if (e < w) {
                if (swap) {
                    y += sy;
                } else {
                    x += sx;
                }

                e += m;
            } else {
                x += sx;
                y += sy;
                e -= w;
            }
        }

        return System.nanoTime() - startTime;
    }

    private static Color getIntensiveColor(Color color, double e, int I) {

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

    public static long Wu(Point start, Point end, Color color) {

        long startTime = System.nanoTime();

        int I = 255;

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        int sx = (int) Math.signum(dx);
        int sy = (int) Math.signum(dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

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
                Color newColor1 = getIntensiveColor(color, r1, I);
                Color newColor2 = getIntensiveColor(color, r2, I);
            } else {
                Color newColor1 = getIntensiveColor(color, r1, I);
                Color newColor2 = getIntensiveColor(color, r2, I);
            }

            X += sx;
            Y += sy * m;
        }

        return System.nanoTime() - startTime;
    }

    private static double fracPart(double num) {
        return num - (long) num;
    }

    private static double revFracPart(double num) {
        return 1 - fracPart(num);
    }
}
