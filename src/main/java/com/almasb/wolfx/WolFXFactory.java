package com.almasb.wolfx;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.wolfx.Config.MAZE_SCALE;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WolFXFactory implements EntityFactory {

    private static final LazyValue<Material> WALL_MAT = new LazyValue<>(() -> {
        var mat = new PhongMaterial();
        mat.setDiffuseColor(Color.GRAY);
        mat.setBumpMap(image("cobblestone_normal.JPG"));

        return mat;
    });

    @Spawns("leftWall")
    public Entity newLeftWall(SpawnData data) {
        var box = new Box(0.2 * MAZE_SCALE, 1 * MAZE_SCALE, 1 * MAZE_SCALE);
        box.setMaterial(WALL_MAT.get());

        return entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(new HitBox(BoundingShape.box3D(box.getWidth(), box.getHeight(), box.getDepth())))
                .view(box)
                .collidable()
                .build();
    }

    @Spawns("topWall")
    public Entity newTopWall(SpawnData data) {
        var box = new Box(1 * MAZE_SCALE, 1 * MAZE_SCALE, 0.2 * MAZE_SCALE);
        box.setMaterial(WALL_MAT.get());

        return entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(new HitBox(BoundingShape.box3D(box.getWidth(), box.getHeight(), box.getDepth())))
                .view(box)
                .collidable()
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox(BoundingShape.box3D(2, 1, 2)))
                .with(new PlayerComponent())
                .collidable()
                .build();
    }
}
