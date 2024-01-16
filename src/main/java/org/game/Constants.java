package org.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public final class Constants {

    public static final Point2D SIZE_OF_BIRD = new Point2D(34, 24);
    public static final Point2D SIZE_OF_GROUND = new Point2D(336, 112);
    public static final Point2D SIZE_OF_PIPE = new Point2D(52, 320);
    public static final Point2D BACKGROUND_SPEED = new Point2D(-40, 0);
    public static final Point2D GAME_SPEED = new Point2D(-100, 0);
    public static final Point2D SIZE_OF_GAME_OVER = new Point2D(192, 42);
    public static final Point2D SIZE_OF_START_SCREEN = new Point2D(184, 267);
    public static final int GAP_BETWEEN_PIPES = 50;
    public static final int GAP_BETWEEN_OBSTACLES = 120;
    public static final int GRAVITY = 10;
    public static final int TERMINAL_VELOCITY = 300;
    public static final double BIRD_TURNING_SPEED = 90;
    public static final double SCORE_FONT_SIZE = 60;
    public static final Point2D BIRD_DEFAULT_POSITION = new Point2D(50, 180);

}
