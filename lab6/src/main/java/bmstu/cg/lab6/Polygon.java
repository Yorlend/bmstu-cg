package bmstu.cg.lab6;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<Line> edges;

    public Polygon() {
        this.edges = new ArrayList<Line>();
    }

    public List<Line> getEdges() {
        return edges;
    }

    public Polygon(List<Line> edges) {
        this.edges = edges;
    }

    public static Polygon buildFrom(List<Polygon> polygons) {
        List<Line> edges = new ArrayList<>();
        for (var poly : polygons) {
            edges.addAll(poly.getEdges());
        }

        return new Polygon(edges);
    }

    public int getBorder() {
        var minMax = findMinMaxX();

        return (minMax.getKey() + minMax.getValue()) / 2;
    }

    private Pair<Integer, Integer> findMinMaxX() {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (var line : edges) {
            min = Integer.min(min, line.getMinX());
            max = Integer.max(max, line.getMaxX());
        }

        return new Pair<Integer, Integer>(min, max);
    }

}
