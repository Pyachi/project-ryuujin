package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.tiles.MovableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.util.Keyboard;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;

public class Player extends GameObject implements Runnable {
    private Direction dir = Direction.UP;

    public Player() {
        setTilePosition(9, 9);
        locZ = 3;
    }

    @Override
    public void run() {
        update();
        calculateMovement();
    }

    private void calculateMovement() {
        int dx = 0, dy = 0;

        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) dy -= 2;
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) dy += 2;
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) dx -= 2;
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) dx += 2;

        if (dy < 0) dir = Direction.UP;
        else if (dy > 0) dir = Direction.DOWN;
        else if (dx < 0) dir = Direction.LEFT;
        else if (dx > 0) dir = Direction.RIGHT;
        texture = dir.texture;

        List<MovableTile> tiles = getTouching(MovableTile.class);

        if (dx != 0) {
            move(dx, 0);
            if (collidesWith(UnpassableTile.class)) move(-dx, 0);
            else if (Keyboard.isKeyPressed(KeyEvent.VK_SHIFT)) for (MovableTile tile : tiles) {
                tile.move(dx, 0);
                if (tile.collidesWith(UnpassableTile.class)) {
                    move(-dx, 0);
                    tile.move(-dx, 0);
                }
            }
            else if (collidesWith(MovableTile.class)) for (MovableTile tile : getCollisions(MovableTile.class)) {
                tile.move(dx, 0);
                if (tile.collidesWith(UnpassableTile.class)) {
                    move(-dx, 0);
                    tile.move(-dx, 0);
                }
            }
        }

        if (dy != 0) {
            move(0, dy);
            if (collidesWith(UnpassableTile.class)) move(0, -dy);
            else if (Keyboard.isKeyPressed(KeyEvent.VK_SHIFT)) for (MovableTile tile : tiles) {
                tile.move(0, dy);
                if (tile.collidesWith(UnpassableTile.class)) {
                    move(0, -dy);
                    tile.move(0, -dy);
                }
            }
            else if (collidesWith(MovableTile.class)) {
                for (MovableTile tile : getCollisions(MovableTile.class)) {
                    tile.move(0, dy);
                    if (tile.collidesWith(UnpassableTile.class)) {
                        move(0, -dy);
                        tile.move(0, -dy);
                    }
                }
            }
        }
    }

//    private Optional<MovableTile> getFacing() {
//        int x, y;
//        switch (dir) {
//            case UP -> {
//                x = getTileX();
//                y = getTileY() - 1;
//            }
//            case DOWN -> {
//                x = getTileX();
//                y = getTileY() + 1;
//            }
//            case LEFT -> {
//                x = getTileX() - 1;
//                y = getTileY();
//            }
//            case RIGHT -> {
//                x = getTileX() + 1;
//                y = getTileY();
//            }
//        }
//        Game.i.getObjectsOfType(MovableTile.class).stream().filter(it -> it.getTileX() == x && it.getTileY() == y)
//    }

    private enum Direction {
        UP(Textures.PLAYER_UP), DOWN(Textures.PLAYER_DOWN), LEFT(Textures.PLAYER_LEFT), RIGHT(Textures.PLAYER_RIGHT);

        public final Textures texture;

        Direction(Textures texture) {
            this.texture = texture;
        }
    }
}
