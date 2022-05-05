package bmstu.cg.lab7;

public class SutherlandCohen {

    static Line cutLine(Line line, Cutter cutter) {

        var p1 = line.getStart();
        var p2 = line.getEnd();

        double m = 0;

        int flag;
        if (p1.getX() == p2.getX()) {
            flag = -1;
        } else {
            m = (double)(p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
            if (m == 0) {
                flag = 0;
            } else {
                flag = 1;
            }
        }


        for (int i = 0; i < 4; i++) {
            int t1 = visionCode(p1, cutter);
            int t2 = visionCode(p2, cutter);

            if ((t1 | t2) == 0) {
                return new Line(p1, p2);
            }

            if ((t1 & t2) != 0) {
                return null;
            }

            if (get(t1, i) == get(t2, i))
                continue;

            if (get(t1, i) == 0) {
                var tmp = p1;
                p1 = p2;
                p2 = tmp;
            }

            if (flag != -1) {
                if (i < 2) {
                    p1.setY((int) (m * (cutter.get(i) - p1.getX()) + p1.getY()));
                    p1.setX(cutter.get(i));
                } else {
                    p1.setX((int) (1 / m * (cutter.get(i) - p1.getY()) + p1.getX()));
                    p1.setY(cutter.get(i));
                }
            } else {
                p1.setY(cutter.get(i));
            }
        }

        return new Line(p1, p2);
    }

    private static int get(int code, int index) {
        return (1 << (3 - index)) & code;
    }

    private static int visionCode(Point point, Cutter cutter) {
        int res = 0;

        if (point.getX() < cutter.get(0))
            res = res | 0b1000;

        if (point.getX() > cutter.get(1))
            res = res | 0b0100;

        if (point.getY() > cutter.get(2))
            res = res | 0b0010;

        if (point.getY() < cutter.get(3))
            res = res | 0b0001;

        return res;
    }
}
