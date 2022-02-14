package com.cg.lab1;

import javafx.geometry.Point2D;

import java.util.List;

public class Solution {

    public static Triangle solve(List<Point2D> points) {
        Triangle result = null;
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < points.size(); i++) {
            Point2D p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point2D p2 = points.get(j);
                for (int k = j + 1; k < points.size(); k++) {
                    Point2D p3 = points.get(k);

                    var triangle = new Triangle(p1, p2, p3);
                    Point2D inter = triangle.getBisectIntersection();
                    double dist = Math.abs(inter.getX()) + Math.abs(inter.getY());

                    if (dist < minDist) {
                        result = triangle;
                        minDist = dist;
                    }
                }
            }
        }

        return result;
    }
}
