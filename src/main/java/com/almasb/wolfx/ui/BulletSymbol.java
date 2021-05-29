package com.almasb.wolfx.ui;

import javafx.scene.shape.Polygon;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BulletSymbol extends Polygon {

    private static final double SCALE = 2.8;

    public BulletSymbol() {
        super(
                5.0 * SCALE, 0.0 * SCALE,
                6.0 * SCALE, 1.0 * SCALE,
                6.0 * SCALE, 9.0 * SCALE,
                4.0 * SCALE, 9.0 * SCALE,
                4.0 * SCALE, 1.0 * SCALE
        );
    }
}
