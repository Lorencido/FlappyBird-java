package org.game;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.Console;
import java.util.Locale;
import java.util.ResourceBundle;

public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private long lastTime;

    private final Game game;
    public DrawingThread(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.game =  new Game(canvas.getWidth(), canvas.getHeight());
    }

    @Override
    public void handle(long now) {
        // put your code here
        if(lastTime > 0)
        {
            double deltaT = (now - lastTime) / 1e9;
            game.simulate(deltaT);
        }
        game.draw(gc);
        lastTime = now;

        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.L) {
                    System.out.println("test");
                    if (game.getLocale().getLanguage().equals("en")) {
                        game.setLocale(new Locale("cs"));
                    } else {
                        game.setLocale(new Locale("en"));
                    }
                }
                game.setResourceBundle(ResourceBundle.getBundle("Texts", game.getLocale()));
            }
        });

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.pressed();
            }
        });

        /*
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                game.pressed();
            }
        });
        */
    }


}
