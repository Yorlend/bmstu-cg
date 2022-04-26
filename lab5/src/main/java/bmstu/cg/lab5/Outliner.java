package bmstu.cg.lab5;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Outliner {

    private final PixelWriter pixelWriter;

    public Outliner(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public void BresenhamInt(Point start, Point end, Color color) {

        int dx = end.getX() - start.getX(), dy = end.getY() - start.getY();
        int sx = Integer.signum(dx), sy = Integer.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        boolean swap = dy > dx;
        if (swap) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
        }
        int e = 2 * dy - dx;

        int x = start.getX(), y = start.getY();
        for (int i = 0; i < dx + 1; i++) {
            pixelWriter.setColor(x, y, color);
            if (e >= 0) {
                if (swap) {
                    x += sx;
                } else {
                    y += sy;
                }
                e -= 2 * dx ;
            }
            if (swap) {
                y += sy;
            } else {
                x += sx;
            }

            e += 2 * dy;
        }
    }
}
