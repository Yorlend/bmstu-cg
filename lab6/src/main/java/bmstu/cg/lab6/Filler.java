package bmstu.cg.lab6;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.Stack;

public class Filler {

    private final Canvas canvas;
    private Color[] pixelBuffer;
    private final Color BACKGROUND_COLOR = Color.rgb(255, 255, 255);

    public Filler(Canvas canvas) {
        this.canvas = canvas;
    }

    public String fill(Color borderColor, Point fuze, Color fuzeColor) {
        createPixelBuffer();
        Stack<Point> fuzeStack = new Stack<>();
        fuzeStack.push(fuze);

        long start = System.nanoTime() / 1000;

        while (!fuzeStack.isEmpty()) {
            var point = fuzeStack.pop();
            int x = point.getX(), y = point.getY();

            if (fuzeColor.equals(readPixel(x, y)))
                continue;

            while (x < canvas.getWidth() && !borderColor.equals(readPixel(x, y))) {
                writePixel(x, y, fuzeColor);
                x++;
            }

            var xRight = x - 1;

            x = point.getX() - 1;
            while(x >= 0 && !borderColor.equals(readPixel(x, y))) {
                writePixel(x, y, fuzeColor);
                x--;
            }

            var xLeft = x + 1;

            if (y > 0)
                searchFuze(fuzeStack, xLeft, xRight, y - 1, fuzeColor, borderColor);
            if (y < canvas.getHeight() - 1)
                searchFuze(fuzeStack, xLeft, xRight, y + 1, fuzeColor, borderColor);
        }

        long end = System.nanoTime() / 1000;

        flushPixelBuffer();

        return Long.toString(end - start) + " мкс";
    }

    public void searchFuze(Stack<Point> stack, int xLeft, int xRight, int y, Color fuzeColor, Color borderColor) {
        int x = xLeft;
        boolean flag;

        while (x <= xRight) {
            flag = false;
            var pixel = readPixel(x, y);
            while (!borderColor.equals(pixel) && !fuzeColor.equals(pixel) && x < xRight) {
                if (!flag)
                    flag = true;
                x++;
                pixel = readPixel(x, y);
            }

            if (flag) {
                if (x == xRight && !borderColor.equals(pixel) && !fuzeColor.equals(pixel))
                    stack.push(new Point(x, y));
                else
                    stack.push(new Point(x - 1, y));
            }

            int xIn = x;

            while ((borderColor.equals(pixel) || fuzeColor.equals(pixel)) && x < xRight) {
                x++;
                pixel = readPixel(x, y);
            }

            if (x == xIn)
                x++;
        }
    }

    private void writePixel(int x, int y, Color color) {
        int index = y * (int) canvas.getWidth() + x;

        pixelBuffer[index] = color;
    }

    private Color readPixel(int x, int y) {
        int index = y * (int) canvas.getWidth() + x;

        return pixelBuffer[index];
    }

    private Color readPixel(Point point) {
        return readPixel(point.getX(), point.getY());
    }

    private void createPixelBuffer() {
        var canvasReader = canvas.snapshot(null, null).getPixelReader();
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        pixelBuffer = new Color[width * height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelBuffer[row * width + col] = canvasReader.getColor(col, row);
            }
        }
    }

    private void flushPixelBuffer() {
        var pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelWriter.setColor(col, row, pixelBuffer[row * width + col]);
            }
        }
    }
}
