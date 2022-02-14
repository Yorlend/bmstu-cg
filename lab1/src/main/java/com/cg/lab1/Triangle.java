package com.cg.lab1;

import javafx.geometry.Point2D;

import java.util.List;

public class Triangle {

    private Point2D firstPoint;
    private Point2D secondPoint;
    private Point2D thirdPoint;

    private Point2D normalizeVector(Point2D beg, Point2D end) {

        return end.subtract(beg).normalize();
    }

    private Point2D getBisector(Point2D p1, Point2D p2, Point2D p3) {

        Point2D v = normalizeVector(p1, p2);
        Point2D w = normalizeVector(p1, p3);

        return v.add(w).normalize();
    }

    private Point2D intersect(Point2D v, Point2D pointV, Point2D w, Point2D pointW) {
        double bx = v.getY() * pointV.getX() - v.getX() * pointV.getY();
        double by = w.getY() * pointW.getX() - w.getX() * pointW.getY();

        double det = v.getX() * w.getY() - v.getY() * w.getX();

        double x = (-w.getX() * bx + v.getX() * by) / det;
        double y = (-w.getY() * bx + v.getY() * by) / det;

        return new Point2D(x, y);
    }

    public Point2D getBisectIntersection() {

        Point2D v1 = getBisector(firstPoint, secondPoint, thirdPoint);
        Point2D v2 = getBisector(secondPoint, thirdPoint, firstPoint);

        return intersect(v1, firstPoint, v2, secondPoint);
    }

    public List<List<Point2D>> getBisects() {

        Point2D b1Dir = getBisector(firstPoint, secondPoint, thirdPoint);
        Point2D b2Dir = getBisector(secondPoint, thirdPoint, firstPoint);
        Point2D b3Dir = getBisector(thirdPoint, firstPoint, secondPoint);

        Point2D b1End = intersect(b1Dir, firstPoint, normalizeVector(secondPoint, thirdPoint), secondPoint);
        Point2D b2End = intersect(b2Dir, secondPoint, normalizeVector(thirdPoint, firstPoint), thirdPoint);
        Point2D b3End = intersect(b3Dir, thirdPoint, normalizeVector(firstPoint, secondPoint), firstPoint);

        return List.of(
                List.of(firstPoint, b1End),
                List.of(secondPoint, b2End),
                List.of(thirdPoint, b3End)
        );
    }

    public Triangle(Point2D firstPoint, Point2D secondPoint, Point2D thirdPoint) {

        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    public List<Point2D> getPoints() {
        return List.of(getFirstPoint(), getSecondPoint(), getThirdPoint());
    }

    public Point2D getFirstPoint() {

        return firstPoint;
    }

    public void setFirstPoint(Point2D firstPoint) {

        this.firstPoint = firstPoint;
    }

    public Point2D getSecondPoint() {

        return secondPoint;
    }

    public void setSecondPoint(Point2D secondPoint) {

        this.secondPoint = secondPoint;
    }

    public Point2D getThirdPoint() {

        return thirdPoint;
    }

    public void setThirdPoint(Point2D thirdPoint) {

        this.thirdPoint = thirdPoint;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f);\n" +
                "(%.2f, %.2f);\n" +
                "(%.2f, %.2f).", firstPoint.getX(), firstPoint.getY(),
                secondPoint.getX(), secondPoint.getY(),
                thirdPoint.getX(), thirdPoint.getY());
    }
}
