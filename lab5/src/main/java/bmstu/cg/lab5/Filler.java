package bmstu.cg.lab5;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Filler {

    private final Canvas canvas;
    private Color[] pixelBuffer;
    private final Color BACKGROUND_COLOR = Color.rgb(255, 255, 255);

    public Filler(Canvas canvas) {
        this.canvas = canvas;
    }

    public String fill(Polygon polygon, Color color) {
        createPixelBuffer();

        long start = System.nanoTime() / 1000;

        int border = polygon.getBorder();

        for (var line : polygon.getEdges()) {
            if (line.isHorizontal()) {
                continue;
            }

            double dx = line.isVertical() ? 0 : line.getDerivative();
            double x = (double) line.getStart().getX();

            for (int y = line.getStart().getY(); y > line.getEnd().getY(); y--) {
                int iX = (int) Math.round(x);
                int step = iX > border ? -1 : 1;
                if (iX < border) {
                    for (int fillX = iX; fillX <= border; fillX++) {
                        invertPixel(fillX, y, color);
                    }
                } else {
                    for (int fillX = iX; fillX > border; fillX--) {
                        invertPixel(fillX, y, color);
                    }
                }
                x += dx;
            }
        }

        long end = System.nanoTime() / 1000;

        flushPixelBuffer();

        return Long.toString(end - start) + " мкс";
    }

    private void invertPixel(int x, int y, Color color) {
        int index = y * (int) canvas.getWidth() + x;
        var pixel = pixelBuffer[index];

        if (pixel != color) {
            pixelBuffer[index] = color;
        } else {
            pixelBuffer[index] = BACKGROUND_COLOR;
        }
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
