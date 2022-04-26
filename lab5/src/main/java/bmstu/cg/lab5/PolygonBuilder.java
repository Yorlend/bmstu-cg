package bmstu.cg.lab5;

import java.util.ArrayList;
import java.util.List;

public class PolygonBuilder {
    private final List<Point> points = new ArrayList<>();
    private final List<Polygon> polygons = new ArrayList<>();

    public void appendPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public void appendHorizontalPoint(int x, int y) {
        if (points.isEmpty()) {
            appendPoint(x, y);
        } else {
            appendPoint(x, points.get(points.size() - 1).getY());
        }
    }

    public void appendVerticalPoint(int x, int y) {
        if (points.isEmpty()) {
            appendPoint(x, y);
        } else {
            appendPoint(points.get(points.size() - 1).getX(), y);
        }
    }

    public void closePolygon() {
        points.add(points.get(0));
        polygons.add(buildFromPoints(points));
        points.clear();
    }

    public Polygon getResult() {
        List<Polygon> result = new ArrayList<>(polygons);
        result.add(buildFromPoints(points));

        return Polygon.buildFrom(result);
    }

    private Polygon buildFromPoints(List<Point> points) {
        List<Line> edges = new ArrayList<>();

        for (int i = 0; i + 1 < points.size(); i++) {
            edges.add(new Line(points.get(i), points.get(i + 1)));
        }

        return new Polygon(edges);
    }
}
