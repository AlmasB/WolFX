package com.almasb.wolfx;

import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PlayerComponent extends Component {

    // how many units do we move atomically
    private static final double MOVE_INCREMENTS = 0.1;

    private double moveSpeed = 45;
    private double speed = 0.0;

    private TransformComponent transform;

    private EntityGroup collidables;

    @Override
    public void onAdded() {
        collidables = getGameWorld().getGroup(EntityType.WALL);
    }

    @Override
    public void onUpdate(double tpf) {
        speed = tpf * moveSpeed;
    }

    public void moveForward() {
        move(() -> transform.moveForwardXZ(MOVE_INCREMENTS), () -> transform.moveBackXZ(MOVE_INCREMENTS));
    }

    public void moveBack() {
        move(() -> transform.moveBackXZ(MOVE_INCREMENTS), () -> transform.moveForwardXZ(MOVE_INCREMENTS));
    }

    public void moveLeft() {
        move(() -> transform.moveLeft(MOVE_INCREMENTS), () -> transform.moveRight(MOVE_INCREMENTS));
    }

    public void moveRight() {
        move(() -> transform.moveRight(MOVE_INCREMENTS), () -> transform.moveLeft(MOVE_INCREMENTS));
    }

    private boolean isColliding = false;

    private void move(Runnable moveFunc, Runnable moveBackFunc) {
        isColliding = false;

        var numIterations = (int)(speed / MOVE_INCREMENTS);

        for (int i = 0; i < numIterations; i++) {
            moveFunc.run();

            collidables.forEach(e -> {
                if (!isColliding) {
                    if (e.isColliding(entity)) {
                        isColliding = true;
                    }
                }
            });

            if (isColliding) {
                moveBackFunc.run();
                return;
            }
        }
    }
}
