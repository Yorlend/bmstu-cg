package bmstu.cg.lab8;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

public class LineModel {

    private SimpleIntegerProperty xs;
    private SimpleIntegerProperty ys;
    private SimpleIntegerProperty xe;
    private SimpleIntegerProperty ye;


    public LineModel(int xs, int ys, int xe, int ye) {
        this.xs = new SimpleIntegerProperty(xs);
        this.ys = new SimpleIntegerProperty(ys);
        this.xe = new SimpleIntegerProperty(xe);
        this.ye = new SimpleIntegerProperty(ye);
    }

    public LineModel(Line line) {
        this.xs = new SimpleIntegerProperty(line.getStart().getX());
        this.ys = new SimpleIntegerProperty(line.getStart().getY());
        this.xe = new SimpleIntegerProperty(line.getEnd().getX());
        this.ye = new SimpleIntegerProperty(line.getEnd().getY());
    }

    public int getXs() {
        return xs.get();
    }

    public SimpleIntegerProperty xsProperty() {
        return xs;
    }

    public void setXs(int xs) {
        this.xs.set(xs);
    }

    public int getYs() {
        return ys.get();
    }

    public SimpleIntegerProperty ysProperty() {
        return ys;
    }

    public void setYs(int ys) {
        this.ys.set(ys);
    }

    public int getXe() {
        return xe.get();
    }

    public SimpleIntegerProperty xeProperty() {
        return xe;
    }

    public void setXe(int xe) {
        this.xe.set(xe);
    }

    public int getYe() {
        return ye.get();
    }

    public SimpleIntegerProperty yeProperty() {
        return ye;
    }

    public void setYe(int ye) {
        this.ye.set(ye);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LineModel that = (LineModel) o;
        return Objects.equals(getXs(), that.getXe()) &&
                Objects.equals(getYs(), that.getYs()) &&
                Objects.equals(getXe(), that.getXe()) &&
                Objects.equals(getYe(), that.getYe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getXs(), getYs(), getXe(), getYe());
    }
}
