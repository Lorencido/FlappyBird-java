package org.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class Game {
    private final double height;
    private final double width;
    private int pickedBackground = generateRandom(0,2);
    public Boolean running = false;
    public Boolean gameStart = false;
    public Boolean gameOver = false;
    //private DrawableSimulable[] objects;
    private ArrayList<DrawableSimulable> objects = new ArrayList<>();;
    private Bird flappy;
    private Pipe[] pipes;
    private ScoreBox[] scoreBoxes;
    private Image gameOverImage;
    private Image startScreenImage;
    private Score score;
    public long startTime = 0;
    private boolean resetPressed = false;

    private ScoreDAO scoreDAO = new ScoreDAO();

    private Locale locale = new Locale("en");
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Texts", locale);

    public Game(double width, double height) {
        this.height = height;
        this.width = width;

        flappy = new Bird(this, Constants.BIRD_DEFAULT_POSITION, new Point2D(0,0), generateRandom(0,3));

        pipes = new Pipe[] {
                new Pipe(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()), generateRandom(80, 320)), Constants.GAME_SPEED),
                new Pipe(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 2, generateRandom(80, 320)), Constants.GAME_SPEED),
                new Pipe(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 3, generateRandom(80, 320)) , Constants.GAME_SPEED),
        };

        scoreBoxes = new ScoreBox[] {
                new ScoreBox(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()), pipes[0].getPosition().getY()), Constants.GAME_SPEED, pipes[0]),
                new ScoreBox(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 2, pipes[1].getPosition().getY()), Constants.GAME_SPEED, pipes[1]),
                new ScoreBox(this, new Point2D(width + (Constants.GAP_BETWEEN_OBSTACLES + Constants.SIZE_OF_PIPE.getX()) * 3, pipes[2].getPosition().getY()), Constants.GAME_SPEED, pipes[2]),
        };
        score = new Score(this, new Point2D(width / 2, 60), new Point2D(0,0), 0, scoreBoxes);
        scoreDAO.createScore(new Score(this, new Point2D(width / 2, 60), new Point2D(0,0), 0, scoreBoxes));

        objects.add(new Background(this, new Point2D(0,0),Constants.BACKGROUND_SPEED, pickedBackground));
        objects.add(new Background(this, new Point2D(width - 1, 0),Constants.BACKGROUND_SPEED, pickedBackground));
        objects.add(pipes[0]);
        objects.add(pipes[1]);
        objects.add(pipes[2]);
        objects.add(scoreBoxes[0]);
        objects.add(scoreBoxes[1]);
        objects.add(scoreBoxes[2]);
        objects.add(new Ground(this, new Point2D(0,height - Constants.SIZE_OF_GROUND.getY()), Constants.GAME_SPEED));
        objects.add(new Ground(this, new Point2D(Constants.SIZE_OF_GROUND.getX() - 2,height - Constants.SIZE_OF_GROUND.getY()), Constants.GAME_SPEED));
        objects.add(flappy);
        objects.add(score);

        /*
        objects = new DrawableSimulable[] {
                new Background(this, new Point2D(0,0),Constants.BACKGROUND_SPEED, pickedBackground),
                new Background(this, new Point2D(width - 1, 0),Constants.BACKGROUND_SPEED, pickedBackground),
                pipes[0],
                pipes[1],
                pipes[2],
                scoreBoxes[0],
                scoreBoxes[1],
                scoreBoxes[2],
                new Ground(this, new Point2D(0,height - Constants.SIZE_OF_GROUND.getY()), Constants.GAME_SPEED),
                new Ground(this, new Point2D(Constants.SIZE_OF_GROUND.getX() - 2,height - Constants.SIZE_OF_GROUND.getY()), Constants.GAME_SPEED),
                flappy,
                score,

        };
        */
        gameOverImage = new Image(getClass().getResourceAsStream("/Sprites/gameover.png"));
        startScreenImage = new Image(getClass().getResourceAsStream("/Sprites/message.png"));

    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void simulate(double deltaT) {
        objects.forEach(i -> {
            i.simulate(deltaT);
            if (i instanceof CollisionAble obj1) {
                objects.forEach(ii -> {
                    if (i != ii && ii instanceof CollisionAble obj2) {
                        if (obj1.overlaps(obj2)) {
                            obj1.overlapsWith(obj2);
                            obj2.overlapsWith(obj1);
                        }
                    }
                });
            }
        });
        /*
            for (DrawableSimulable i: objects) {
                i.simulate(deltaT);
                if (i instanceof CollisionAble obj1) {
                    for (DrawableSimulable ii: objects) {
                        if (i != ii && ii instanceof CollisionAble obj2) {
                            if (obj1.overlaps(obj2)) {
                                obj1.overlapsWith(obj2);
                                obj2.overlapsWith(obj1);
                            }
                        }
                    }
                }
            }

         */

        if (LocalTime.now().toSecondOfDay() - startTime >= 1 && resetPressed) {
            save();
            pipes[0].resetPosition(1);
            pipes[1].resetPosition(2);
            pipes[2].resetPosition(3);
            scoreBoxes[0].resetPosition(1);
            scoreBoxes[1].resetPosition(2);
            scoreBoxes[2].resetPosition(3);
            flappy.reset();
            score.setValue(0);
            gameOver = false;
            gameStart = false;
            resetPressed = false;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0,0, width, height);

        for(DrawableSimulable obj: objects) {
            obj.draw(gc);
        }

        if(!gameStart) {
            gc.drawImage(startScreenImage, width / 2 - Constants.SIZE_OF_START_SCREEN.getX() / 2, 100, Constants.SIZE_OF_START_SCREEN.getX(), Constants.SIZE_OF_START_SCREEN.getY());

        } else if (gameOver) {
            gc.drawImage(gameOverImage, width / 2 - Constants.SIZE_OF_GAME_OVER.getX() / 2, height / 3, Constants.SIZE_OF_GAME_OVER.getX(), Constants.SIZE_OF_GAME_OVER.getY());
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Sans Serif", FontWeight.BOLD, 20));
            gc.fillText(getResourceBundle().getString("highScore"), width / 2 - 90, height / 2);
        }
        gc.save();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Sans Serif", FontWeight.BOLD, 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(getResourceBundle().getString("changeLanguage"), 60, 15);
        gc.restore();
    }
    public int generateRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private int load() {
        FileReader fileReader;
        int result;

        try {
            fileReader = new FileReader("high_score.txt");

            while((result = Integer.valueOf(fileReader.read()))  != -1) {
                System.out.println(result);
                return result;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public void save() {
        FileWriter fileWriter;
        int highScore = 0;
        try {
            if(load() != -1) {
                highScore = load();
                System.out.println(highScore);
            }
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

        try {
            fileWriter = new FileWriter("high_score.txt");
            if(score.getValue() > highScore) {
                fileWriter.write(String.valueOf(score.getValue()));
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addScores() {
        score.addScore();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            scoreDAO.updateScore(score);
        });
    }
    public void pressed() {
        flappy.flap();
        if(!running && !gameOver && !gameStart) {
            running = true;
            gameStart = true;
        } else if(gameOver) {
            startTime = LocalTime.now().toSecondOfDay();
            resetPressed = true;
        }

    }
}
