package bmstu.cg.lab8;

public class CyrusBeck {

    static Line cutLine(Line line, Cutter cutter) {

        var p1 = line.getStart();
        var p2 = line.getEnd();

        double tn = 0, tv = 1;

        var D = Vector.vectorize(p1, p2);

        if (!cutter.isConvex() || cutter.selfIntersects()) { return null; }

        for (var edge : cutter) {
            var edgeDir = new Vector(edge);
            var f = edge.getStart();

            var w = Vector.vectorize(f, p1);
            var n = edgeDir.getNormal();

            double Wck = w.dot(n);
            double Dck = D.dot(n);

            if (Dck == 0 && Wck < 0)
                return null;

            double t = - Wck / Dck;

            if (Dck > 0) {
                if (t > 1)
                    return null;

                tn = Double.max(tn, t);
            } else if (Dck <= 0) {
                if (t < 0)
                    return null;
                
                tv = Double.min(tv, t);
            }
        }

        if (tn > tv) { return null; }

        p2 = p1.add(D.multiply(tv));
        p1 = p1.add(D.multiply(tn));

        return new Line(p1, p2);
    }
}
