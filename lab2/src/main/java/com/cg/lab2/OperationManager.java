package com.cg.lab2;

import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

public class OperationManager {

    private final List<Affine> history = new ArrayList<>();

    public void addOperation(Affine op) {

        history.add(op);
    }

    public Affine getTransformation() {

        Affine res = new Affine(1, 0, 0, 0, 1, 0);

        history.forEach(res::prepend);

        return res;
    }

    public void rewindTransformation() {

        if (history.size() > 0) {

            history.remove(history.size() - 1);
        }
    }

    public void resetTransformation() {

        history.clear();
    }
}
