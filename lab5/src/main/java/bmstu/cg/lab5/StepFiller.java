package bmstu.cg.lab5;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class StepFiller {
    private final Canvas canvas;
    private int[] pixelBuffer;
    private final int BACKGROUND_COLOR = 0xFFFFFFFF;

    private int color;
    private Polygon polygon;
    private int border;
    private int lineIndex;
    private int endY;
    private int y;
    private double x;
    private double dx;

    public StepFiller(Canvas canvas) {
        this.canvas = canvas;
    }

    public void startFill(Polygon polygon, Color color) {
        this.polygon = polygon;
        this.color = colorToInt(color);
        border = polygon.getBorder();
        lineIndex = -1;
        goToNextLine();
        createPixelBuffer();
    }

    public boolean hasNext() {
        return lineIndex < polygon.getEdges().size();
    }

    public void stepFill() {
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
        flushPixelBuffer(y);
        y--;

        if (y <= endY) {
            goToNextLine();
        }
    }

    private void invertPixel(int x, int y, int color) {
        int index = y * (int) canvas.getWidth() + x;
        var pixel = pixelBuffer[index];

        if (pixel != color) {
            pixelBuffer[index] = color;
        } else {
            pixelBuffer[index] = BACKGROUND_COLOR;
        }
    }

    private void goToNextLine() {
        var lines = polygon.getEdges();
        do {
            lineIndex++;
        } while (lineIndex < lines.size() && lines.get(lineIndex).isHorizontal());
        if (lineIndex >= lines.size())
            return;

        Line line = lines.get(lineIndex);
        dx = line.isVertical() ? 0 : line.getDerivative();
        x = (double) line.getStart().getX();

        y = line.getStart().getY();
        endY = line.getEnd().getY();
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
