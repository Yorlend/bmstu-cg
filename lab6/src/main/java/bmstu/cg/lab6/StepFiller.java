package bmstu.cg.lab6;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.Stack;

public class StepFiller {
    private final Canvas canvas;
    private int[] pixelBuffer;
    private final int BACKGROUND_COLOR = 0xFFFFFFFF;

    private int color;
    private final Stack<Point> stack = new Stack<>();

    public StepFiller(Canvas canvas) {
        this.canvas = canvas;
    }

    public void startFill(Point point, Color color) {
        this.stack.push(point);
        this.color = colorToInt(color);
        createPixelBuffer();
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public void stepFill() {
        if (!stack.isEmpty()) {
            var point = stack.pop();
            int x = point.getX(), y = point.getY();

            if (color == readPixel(x, y))
                return;

            while (x < canvas.getWidth() && readPixel(x, y) == BACKGROUND_COLOR) {
                writePixel(x, y, color);
                x++;
            }

            var xRight = x - 1;

            x = point.getX() - 1;
            while(x >= 0 && readPixel(x, y) == BACKGROUND_COLOR) {
                writePixel(x, y, color);
                x--;
            }

            var xLeft = x + 1;

            if (y > 0)
                searchFuze(xLeft, xRight, y - 1);
            if (y < canvas.getHeight() - 1)
                searchFuze(xLeft, xRight, y + 1);

            flushPixelBuffer(y);
        }
    }

    public void searchFuze(int xLeft, int xRight, int y) {
        int x = xLeft;
        boolean flag;

        while (x <= xRight) {
            flag = false;
            var pixel = readPixel(x, y);
            while (x < xRight && pixel == BACKGROUND_COLOR) {
                if (!flag)
                    flag = true;
                x++;
                pixel = readPixel(x, y);
            }

            if (flag) {
                if (x == xRight && pixel == BACKGROUND_COLOR)
                    stack.push(new Point(x, y));
                else
                    stack.push(new Point(x - 1, y));
            }

            int xIn = x;

            while (x < xRight && pixel != BACKGROUND_COLOR) {
                x++;
                pixel = readPixel(x, y);
            }

            if (x == xIn)
                x++;
        }
    }

    private void writePixel(int x, int y, int color) {
        int index = y * (int) canvas.getWidth() + x;
        pixelBuffer[index] = color;
    }

    private int readPixel(int x, int y) {
        int index = y * (int) canvas.getWidth() + x;
        return pixelBuffer[index];
    }

    private void createPixelBuffer() {
        var canvasReader = canvas.snapshot(null, null).getPixelReader();
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        pixelBuffer = new int[width * height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelBuffer[row * width + col] = canvasReader.getArgb(col, row);
            }
        }
    }

    private void flushPixelBuffer(int y) {
        var pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        int width = (int) canvas.getWidth();

        for (int col = 0; col < width; col++) {
            pixelWriter.setArgb(col, y, pixelBuffer[(y) * width + col]);
        }
    }

    private int colorToInt(Color color) {
        int red = ((int)(255 * color.getRed()) << 16) & 0x00FF0000;
        int green = ((int)(255 * color.getGreen()) << 8) & 0x0000FF00;
        int blue = (int)(255 * color.getBlue()) & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}

