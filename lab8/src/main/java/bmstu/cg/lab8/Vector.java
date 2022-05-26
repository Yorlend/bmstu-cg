package bmstu.cg.lab8;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Line line) {
        this.x = line.getEnd().getX() - line.getStart().getX();
        this.y = line.getEnd().getY() - line.getStart().getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        double len = length();

        return new Vector(x / len, y / len);
    }

    public static Vector vectorize(Point p1, Point p2) {
        return new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public static Vector direction(Point p1, Point p2) {
        return vectorize(p1, p2).normalize();
    }

    public double dot(Vector vec) {
        return x * vec.x + y * vec.y;
    }

    public double cross(Vector vec) {
        return x * vec.y - y * vec.x;
    }

    public Vector getNormal() {
        return (new Vector(-y, x)).normalize();
    }

    public Vector multiply(double n) {
        return new Vector(x * n, y * n);
    }

    public Vector add(Vector vec) {
        return new Vector(x + vec.x, y + vec.y);
    }
}
