package org.game;

import javafx.geometry.Rectangle2D;

import java.util.Arrays;

public interface CollisionAble {
    default boolean overlaps(CollisionAble other) {
        for(Rectangle2D collider1: getCollisionBox()) {
            for(Rectangle2D collider2: other.getCollisionBox()) {
                if(collider1.intersects(collider2)) {
                 return true;
                }
            }
        }
        return false;
    }
    void overlapsWith(CollisionAble other);

    Rectangle2D[] getCollisionBox();
}
