package org.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Pipe implements DrawableSimulable, CollisionAble{
    @NonNull
    private final Game game;
    @NonNull
    private Point2D position;
    @NonNull
    private Point2D velocity;
    private final Image pipeImage = new Image(getClass().getResourceAsStream("/Sprites/pipe-green.png"));;

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.drawImage(pipeImage, position.getX(), position.getY() + Constants.GAP_BETWEEN_PIPES, Constants.SIZE_OF_PIPE.getX(), Constants.SIZE_OF_PIPE.getY());
        gc.setTransform(new Affine(Affine.rotate(180, position.getX() + Constants.SIZE_OF_PIPE.getX() / 2, position.getY())));
        gc.drawImage(pipeImage, position.getX(), position.getY() + Constants.GAP_BETWEEN_PIPES, Constants.SIZE_OF_PIPE.getX() , Constants.SIZE_OF_PIPE.getY());
        gc.restore();
    }
    @Override
    public void simulate(double deltaT) {
        if(game.running) {
            position = position.add(velocity.multiply(deltaT));
            if (position.getX() + Constants.SIZE_OF_PIPE.getX() <= 0) {
                position = new Point2D(position.getX() + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 3, game.generateRandom(80, 320));
            }
        }
    }

    @Override
    public Rectangle2D[] getCollisionBox() {
        return new Rectangle2D[] {
                new Rectangle2D(position.getX(),position.getY() + Constants.GAP_BETWEEN_PIPES, Constants.SIZE_OF_PIPE.getX() , Constants.SIZE_OF_PIPE.getY()),
                new Rectangle2D(position.getX(),position.getY() - Constants.GAP_BETWEEN_PIPES - Constants.SIZE_OF_PIPE.getY(), Constants.SIZE_OF_PIPE.getX() , Constants.SIZE_OF_PIPE.getY()),
        };
    }
    public void overlapsWith(CollisionAble other) {

    }

    public double posY() {
        return position.getY();
    }

    public void resetPosition(double x) {
       position = new Point2D(game.getWidth() + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * x, game.generateRandom(80, 320));
    }

}
