package org.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ScoreBox implements DrawableSimulable, CollisionAble{
    @NonNull
    private final Game game;
    @NonNull
    private Point2D position;
    @NonNull
    private Point2D velocity;
    @NonNull
    private Pipe pipes;

    private HitListener hitListener;
    private boolean wasRecentlyHit = false;

    public void setHitListener(HitListener listener) {
        this.hitListener = listener;
    }


    public void notifyListener() {
        hitListener.hit();
    }

    @Override
    public void overlapsWith(CollisionAble other) {
        if(other instanceof Bird && wasRecentlyHit == false) {
            game.addScores();
            notifyListener();
            wasRecentlyHit = true;
        }
    }

    @Override
    public Rectangle2D[] getCollisionBox() {
        return new Rectangle2D[] {
                new Rectangle2D(position.getX(),position.getY() - Constants.GAP_BETWEEN_PIPES, Constants.SIZE_OF_PIPE.getX() , Constants.GAP_BETWEEN_PIPES * 2),
        };
    }

    @Override
    public void simulate(double deltaT) {
        if(game.running) {
            position = position.add(velocity.multiply(deltaT));
            if (position.getX() + Constants.SIZE_OF_PIPE.getX() <= 0) {
                position = new Point2D(position.getX() + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 3, pipes.posY());
                wasRecentlyHit = false;
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    public void resetPosition(double x) {
        position = new Point2D(game.getWidth() + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * x, pipes.posY());
    }
}
