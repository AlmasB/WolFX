package com.almasb.wolfx;

import com.almasb.fxgl.animation.AnimatedValue;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.pathfinding.maze.Maze;
import com.almasb.fxgl.ui.FontType;
import com.almasb.wolfx.ui.Bar;
import com.almasb.wolfx.ui.BulletSymbol;
import javafx.geometry.Pos;
import javafx.scene.PointLight;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.wolfx.Config.MAZE_SCALE;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WolFXApp extends GameApplication {

    private Entity player;
    private PlayerComponent playerComponent;

    private Camera3D camera3D;
    private TransformComponent cameraTransform;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.set3D(true);
//        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> {
            playerComponent.moveForward();
        });
        onKey(KeyCode.S, () -> {
            playerComponent.moveBack();
        });
        onKey(KeyCode.A, () -> {
            playerComponent.moveLeft();
        });
        onKey(KeyCode.D, () -> {
            playerComponent.moveRight();
        });

        onKey(KeyCode.L, () -> {
            getGameController().exit();
        });

        onKeyDown(KeyCode.F, () -> {

        });
    }

    @Override
    protected void initGame() {
        camera3D = getGameScene().getCamera3D();
        camera3D.setMoveSpeed(25);

        cameraTransform = getGameScene().getCamera3D().getTransform();

        getGameScene().setBackgroundColor(Color.LIGHTBLUE);

        getGameScene().setFPSCamera(true);
        getGameScene().setCursorInvisible();

        getGameWorld().addEntityFactory(new WolFXFactory());

        initMaze();

        player = spawn("player", 5, 0, 0);
        playerComponent = player.getComponent(PlayerComponent.class);

        // camera follows player translation
        cameraTransform.xProperty().bind(player.xProperty());
        cameraTransform.yProperty().bind(player.yProperty());
        cameraTransform.zProperty().bind(player.zProperty());
    }

    private void initMaze() {
        Maze maze = new Maze(20, 20);
        maze.forEach(cell -> {
            if (cell.hasLeftWall()) {
                spawn("leftWall", cell.getX() * MAZE_SCALE, 0, cell.getY() * MAZE_SCALE);
            }

            if (cell.hasTopWall()) {
                spawn("topWall", (cell.getX() + 0.5) * MAZE_SCALE, 0, (cell.getY() - 0.5) * MAZE_SCALE);
            }
        });

        // right
//        Box cubeR = new Box(0.2 * MAZE_SCALE, 1 * MAZE_SCALE, 1 * maze.getHeight() * MAZE_SCALE);
//        cubeR.setMaterial(mat);
//
//        cubeR.setTranslateX(maze.getWidth() * MAZE_SCALE);
//        cubeR.setTranslateY(0);
//        cubeR.setTranslateZ((cubeR.getDepth() / 2 - 0.5) * MAZE_SCALE);
//
//        root.getChildren().addAll(cubeR);
//
//        // bot
//        Box cubeB = new Box(1 * maze.getWidth() * MAZE_SCALE, 1 * MAZE_SCALE, 0.2 * MAZE_SCALE);
//
//        cubeB.setTranslateX((cubeB.getWidth() / 2) * MAZE_SCALE);
//        cubeB.setTranslateY(0);
//        cubeB.setTranslateZ((maze.getHeight() - 0.5) * MAZE_SCALE);
//
//        root.getChildren().addAll(cubeB);

        var groundMat = new PhongMaterial();
        groundMat.setDiffuseColor(Color.BLACK);

        Box ground = new Box(maze.getWidth() * MAZE_SCALE, 0.2 * MAZE_SCALE, maze.getHeight() * MAZE_SCALE);
        ground.setMaterial(groundMat);




        var light1 = new PointLight();
        light1.setTranslateX(-150);
        light1.setTranslateZ(-150);
        light1.setTranslateY(-45);
        light1.setConstantAttenuation(0.6);

        var light2 = new PointLight();
        light2.setTranslateX(maze.getWidth() * MAZE_SCALE + 150);
        light2.setTranslateZ(maze.getHeight() * MAZE_SCALE + 150);
        light2.setTranslateY(-45);
        light2.setConstantAttenuation(0.6);

        var light3 = new PointLight();
        light3.setConstantAttenuation(1);


        entityBuilder()
                .at((maze.getWidth() / 2) * MAZE_SCALE, (0.1 + 0.5) * MAZE_SCALE, (maze.getHeight() / 2 - 0.5) * MAZE_SCALE)
                .view(ground)
                .view(light1)
                .view(light2)
                .buildAndAttach();

//        var e = entityBuilder()
//                .view(light3)
//                .buildAndAttach();
//
//        e.xProperty().bind(camera3D.getTransform().xProperty());
//        e.yProperty().bind(camera3D.getTransform().yProperty());
//        e.zProperty().bind(camera3D.getTransform().zProperty());
    }

    @Override
    protected void initUI() {
        var text = getUIFactoryService().newText("Controls:\n" +
                        "WASD - Move\n" +
                        "Move mouse - Rotate Camera\n" +
                        "L - Exit game",
                Color.BLACK, 24.0);

        //addUINode(text, 50, 50);

        var hpBar = new Bar(300, 15, Color.LIGHTGREEN);
        hpBar.setValue(90);

        var armorBar = new Bar(300, 15, Color.BLUE);
        armorBar.setValue(40);

        addUINode(armorBar, 25, getAppHeight() - 70);
        addUINode(hpBar, 25, getAppHeight() - 50);

        var bulletsText = getUIFactoryService().newText("42", Color.WHITE, FontType.UI, 26.0);
        bulletsText.setStroke(Color.BLACK);

        var symbol = new BulletSymbol();
        symbol.setFill(Color.WHITE);
        symbol.setStroke(Color.BLACK);

        var bullets = new HBox(5, symbol, bulletsText);
        bullets.setAlignment(Pos.TOP_CENTER);

        addUINode(bullets, 25, getAppHeight() - 110);

        animationBuilder()
                .repeatInfinitely()
                .autoReverse(true)
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .animate(new AnimatedValue<>(0.0, 100.0))
                .onProgress(value -> {
                    hpBar.setValue(value);
                })
                .buildAndPlay();
    }

    @Override
    protected void onUpdate(double tpf) {
        // TODO: tmp hack until FXGL provides public API to recalc direction3D
        player.getTransformComponent().lookAt(cameraTransform.getPosition3D().add(cameraTransform.getDirection3D().multiply(50)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
