package bmstu.cg.lab8;

public class Line {

    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
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

    public boolean intersects(Line other) {

        int x1 = start.getX(), y1 = start.getY();
        int x2 = end.getX(), y2 = end.getY();

        int x3 = other.start.getX(), y3 = other.start.getY();
        int x4 = other.end.getX(), y4 = other.end.getY();

        if (x1 > x2) {
            x1 ^= x2;
            x2 ^= x1;
            x1 ^= x2;

            y1 ^= y2;
            y2 ^= y1;
            y1 ^= y2;
        }

        if (x3 > x4) {
            x3 ^= x4;
            x4 ^= x3;
            x3 ^= x4;

            y3 ^= y4;
            y4 ^= y3;
            y3 ^= y4;
        }

        double k1 = y1 == y2 ? 0 : (double) (y2 - y1) / (x2 - x1);
        double k2 = y3 == y4 ? 0 : (double) (y4 - y3) / (x4 - x3);

        if (k1 == k2) { return false; }

        double b1 = y1 - k1 * x1;
        double b2 = y3 - k2 * x3;

        int resX = (int) Math.round((b2 - b1) / (k1 - k2));
        // int resY = (int) Math.round(k1 * resX - b1);

        if (resX > x1 && resX < x2 && resX > x3 && resX < x4)
            return true;

        return false;
    }
}
