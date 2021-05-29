package com.almasb.wolfx.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Bar extends Pane {

    private Rectangle bg;
    private Rectangle inner;

    public Bar(double w, double h, Color fill) {
        bg = new Rectangle(w, h);
        bg.setFill(Color.color(0, 0, 0, 0.25));
        bg.setStrokeWidth(1.4);
        bg.setStroke(Color.WHITE);

        var outer = new Rectangle(w, h);
        outer.setFill(Color.TRANSPARENT);
        outer.setStroke(Color.BLACK);

        inner = new Rectangle(w - 4, h - 4);
        inner.setFill(fill);
        inner.setTranslateX(2);
        inner.setTranslateY(2);
//        inner.setStrokeWidth(0.5);
//        inner.setStrokeType(StrokeType.INSIDE);
//        inner.setStroke(Color.WHITE);

        getChildren().addAll(bg, outer, inner);
    }

    public void setValue(double value) {
        inner.setWidth(value / 100.0 * (bg.getWidth() - 2.8 - 1));
    }
}
