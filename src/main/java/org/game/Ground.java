package org.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Ground implements DrawableSimulable, CollisionAble{
    @NonNull
    private final Game game;
    @NonNull
    private Point2D position;
    @NonNull
    private Point2D velocity;
    private final Image groundImage = new Image(getClass().getResourceAsStream("/Sprites/base.png"));;

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(groundImage, position.getX(), position.getY(), Constants.SIZE_OF_GROUND.getX(), Constants.SIZE_OF_GROUND.getY());
    }
    @Override
    public void simulate(double deltaT) {
        if(game.running) {
            position = position.add(velocity.multiply(deltaT));
            if(position.getX() + Constants.SIZE_OF_GROUND.getX() <= 0) {
                position = new Point2D(Constants.SIZE_OF_GROUND.getX() - 4, game.getHeight() - Constants.SIZE_OF_GROUND.getY());
            }
        }
    }
    @Override
    public Rectangle2D[] getCollisionBox() {
        return new Rectangle2D[] {
                new Rectangle2D(position.getX(), position.getY(), Constants.SIZE_OF_GROUND.getX(), Constants.SIZE_OF_GROUND.getY()),
        };
    }
    public void overlapsWith(CollisionAble other) {

    }
}
