package bmstu.cg.lab7;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Cutter implements Iterable<Integer> {

    private final int[] cutter = new int[4];

    public Cutter(int xLeft, int xRight, int yDown, int yUp) {
        this.cutter[0] = xLeft;
        this.cutter[1] = xRight;
        this.cutter[2] = yDown;
        this.cutter[3] = yUp;
    }

    public int size() {
        return cutter.length;
    }

    public int get(int index) {
        return cutter[index];
    }

    @Override
    public Iterator<Integer> iterator() {
        return new CutterIterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Iterable.super.spliterator();
    }

    class CutterIterator implements Iterator<Integer> {

        private int index = 0;

        public boolean hasNext() {
            return index < size();
        }

        public Integer next() {
            return get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException("no >_<");
        }
    }
}
