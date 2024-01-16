package org.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Score implements DrawableSimulable{
    @Transient
    private Game game;
    @Transient
    private Point2D position;
    @Transient
    private Point2D speed;

    private int value;

    @Transient
    private transient ScoreBox[] scoreBoxes;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    protected Score(Game game, Point2D position, Point2D speed, int value, ScoreBox[] scoreBoxes) {
        this.game = game;
        this.position = position;
        this.speed = speed;
        this.value = value;

        this.scoreBoxes = scoreBoxes;

        for (ScoreBox box: scoreBoxes) {
            box.setHitListener(new HitListener() {
                @Override
                public void hit() {
                    addScore();
                }
            });
        }
    }

    @Override
    public void simulate(double deltaT) {

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Sans Serif", FontWeight.BOLD, Constants.SCORE_FONT_SIZE));
        gc.fillText(String.valueOf(value), position.getX() - 20 * (String.valueOf(value).length()), position.getY());
        gc.restore();
    }

    public void addScore() {
        this.value++;
    }

    @Override
    public String toString() {
        return game.getResourceBundle().getString("highScore") + ": " + value;
    }

}
