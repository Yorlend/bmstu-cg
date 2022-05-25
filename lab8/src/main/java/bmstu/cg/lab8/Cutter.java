package bmstu.cg.lab8;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

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

    public void add(Line line) {
        lines.add(line);
    }

    public int size() {
        return lines.size();
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
}
