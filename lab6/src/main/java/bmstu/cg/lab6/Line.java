package bmstu.cg.lab6;

public class Line {

    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        if (start.getY() < end.getY()) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }

    public Line(int xs, int ys, int xe, int ye) {
        this(new Point(xs, ys), new Point(xe, ye));
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public int getMinX() {
        return Integer.min(start.getX(), end.getX());
    }

    public int getMaxX() {
        return Integer.max(start.getX(), end.getX());
    }

    public double getDerivative() {
        return (double) (end.getX() - start.getX()) / Math.abs(end.getY() - start.getY());
    }

    public boolean isVertical() {
        return start.getX() == end.getX();
    }

    public boolean isHorizontal() {
        return start.getY() == end.getY();
    }
}
