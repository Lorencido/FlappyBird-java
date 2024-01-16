package org.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@RequiredArgsConstructor
public class Background implements DrawableSimulable{
    @NonNull
    private final Game game;
    @NonNull
    private Point2D position;
    @NonNull
    private Point2D velocity;
    @NonNull
    private final int pickedBackground;
    private static final Point2D BACKGROUND_SIZE = new Point2D(288, 512);
    private final Image[] backgroundImage = new Image[]{
            new Image(getClass().getResourceAsStream("/Sprites/background-day.png")),
            new Image(getClass().getResourceAsStream("/Sprites/background-night.png")),
    };

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(backgroundImage[pickedBackground], position.getX(), position.getY(), BACKGROUND_SIZE.getX(), BACKGROUND_SIZE.getY());
    }

    @Override
    public void simulate(double deltaT) {
        if(game.running) {
            position = position.add(velocity.multiply(deltaT));
            if (position.getX() + BACKGROUND_SIZE.getX() <= 0) {
                position = new Point2D(BACKGROUND_SIZE.getX() - 2, 0);
            }
        }
    }
}
