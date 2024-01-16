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

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class Bird implements DrawableSimulable, CollisionAble{
    @NonNull
    private final Game game;
    @NonNull
    private Point2D position;
    @NonNull
    private Point2D velocity;
    @NonNull
    private final int pickedBird;
    private Image[] birdImage = new Image[]{
            new Image(getClass().getResourceAsStream("/Sprites/red-bird.gif")),
            new Image(getClass().getResourceAsStream("/Sprites/blue-bird.gif")),
            new Image(getClass().getResourceAsStream("/Sprites/yellow-bird.gif")),
    };;
    private int vertical_speed = 0;
    private Boolean accelerate = true;
    private double angle = 0;
    private int direction = 1;
    private double timer = 0;
    private int timeStep = 100;

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setTransform(new Affine(Affine.rotate(angle, position.getX() + Constants.SIZE_OF_BIRD.getX() / 2, position.getY() + Constants.SIZE_OF_BIRD.getY() / 2)));
        gc.drawImage(birdImage[pickedBird], position.getX(), position.getY(), Constants.SIZE_OF_BIRD.getX(), Constants.SIZE_OF_BIRD.getY());
        gc.restore();

    }

    @Override
    public Rectangle2D[] getCollisionBox() {
        return new Rectangle2D[] {
                new Rectangle2D(position.getX(), position.getY(), Constants.SIZE_OF_BIRD.getX(), Constants.SIZE_OF_BIRD.getY()),
        };
    }
    @Override
    public void simulate(double deltaT) {
        if(game.gameStart) {
            if (accelerate) {
                this.vertical_speed = this.vertical_speed + Constants.GRAVITY;
                if (this.vertical_speed > Constants.TERMINAL_VELOCITY) {
                    this.vertical_speed = Constants.TERMINAL_VELOCITY;
                }
                velocity = new Point2D(0, vertical_speed);
                position = position.add(velocity.multiply(deltaT));
            } else {
                angle = 90;
            }
            if (angle <= 90 && angle >= -30) {
                angle = angle + direction * Constants.BIRD_TURNING_SPEED * deltaT;
            } else if (angle > 90) {
                angle = 90;
            } else if (angle < -30) {
                angle = -30;
            }
        }
    }
    public void flap() {
        if(game.gameStart) {
            this.vertical_speed = -200;
            if (angle <= 90 && angle >= -30) {
                angle = angle - 90;
            }
        }
    }
    @Override
    public void overlapsWith(CollisionAble other) {
        if(other instanceof Ground) {
            accelerate = false;
            game.running = false;
            game.gameOver = true;
        } else if(other instanceof Pipe) {
            game.running = false;
            game.gameOver = true;
        }
    }

    public void reset() {
        position = new Point2D(Constants.BIRD_DEFAULT_POSITION.getX(), Constants.BIRD_DEFAULT_POSITION.getY());
        angle = 0;
        accelerate = true;
    }

     public double getPositionY() {
        return position.getY();

     }
}
