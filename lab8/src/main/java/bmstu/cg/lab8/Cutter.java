package bmstu.cg.lab8;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Cutter implements Iterable<Line> {
    
    private final List<Line> lines;
    private Point start;

    public Cutter() {
        lines = new ArrayList<>();
    }

    public void appendPoint(ObservableList<LineModel> models, int x, int y) {
        var point = new Point(x, y);
        if (start == null) {
            start = point;
        } else {
            var line = new Line(start, point);
            lines.add(line);
            models.add(new LineModel(line));
            start = point;
        }
    }

    public int appendHorizontalPoint(ObservableList<LineModel> models, int x, int y) {
        var point = new Point(x, y);
        if (start == null) {
            start = point;
            return y;
        }

        y = start.getY();
        point.setY(y);
        var line = new Line(start, point);
        lines.add(line);
        models.add(new LineModel(line));
        start = point;

        return y;
    }

    public int appendVerticalPoint(ObservableList<LineModel> models, int x, int y) {
        var point = new Point(x, y);
        if (start == null) {
            start = point;
            return x;
        }

        x = start.getX();
        point.setX(x);
        var line = new Line(start, point);
        lines.add(line);
        models.add(new LineModel(line));
        start = point;

        return x;
    }

    public void close(ObservableList<LineModel> models) {
        var line = new Line(getLast().getEnd(), get(0).getStart());

        lines.add(line);
        models.add(new LineModel(line));
    }

    public void add(Line line) {
        lines.add(line);
    }

    public void clear() {
        lines.clear();
        start = null;
    }

    public int size() {
        return lines.size();
    }

    public Line getLast() {
        return lines.isEmpty() ? null : get(lines.size() - 1);
    }

    public Line get(int index) {
        return lines.get(index);
    }

    @Override
    public Iterator<Line> iterator() {
        return new CutterIterator();
    }

    @Override
    public void forEach(Consumer<? super Line> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Line> spliterator() {
        return Iterable.super.spliterator();
    }

    class CutterIterator implements Iterator<Line> {

        private int index = 0;

        public boolean hasNext() {
            return index < size();
        }

        public Line next() {
            return get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException("no >_<");
        }
    }

    public boolean selfIntersects() {

        for (int i = 0; i < size() - 1; i++) {
            for (int j = i + 1; j < size(); j++) {
                if (get(i).intersects(get(j)))
                    return true;
            }
        }

        return false;
    }

    public boolean isConvex() {
        Vector vec2 = new Vector(get(0));
        Vector vec1 = new Vector(getLast());

        int crossProd = vec1.cross(vec2) > 0 ? 1 : -1;

        for (int i = 1; i < size(); i++) {
            vec1 = vec2;
            vec2 = new Vector(get(i));

            if (crossProd * vec1.cross(vec2) < 0)
                return false;
        }

        if (crossProd < 0) { reverseCutter(); }

        return true;
    }

    public void reverseCutter() {
        for (var line : this) {
            var start = line.getStart();
            line.setStart(line.getEnd());
            line.setEnd(start);
        }
    }
}
