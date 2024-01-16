package org.game;

import javafx.geometry.Point2D;

public abstract class GameObject implements DrawableSimulable{
    protected final Game game;
    protected Point2D velocity;
    protected Point2D position;

    public GameObject(Game game, Point2D position, Point2D velocity) {
        this.game = game;
        this.position = position;
        this.velocity = velocity;
    }
}
