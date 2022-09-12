package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.tiles.Crate;
import com.cs321.team1.framework.objects.tiles.WallTile;
import com.cs321.team1.util.Keyboard;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Predicate;

public class Player extends GameObject implements Runnable {
    private PlayerDirection dir = PlayerDirection.UP;
    private Crate grabbed = null;
    private int speed = 2;

    public Player() {
        setTilePosition(9, 9);
        locZ = 3;
    }

    @Override
    public void run() {
        update();
        calculateMovement();
        if (grabbed == null && Keyboard.isKeyPressed(KeyEvent.VK_SHIFT)) grabFacingTile();
        if (grabbed != null && (!Keyboard.isKeyPressed(KeyEvent.VK_SHIFT) || grabbed.isKilled())) {
            grabbed.grabbed = false;
            grabbed = null;
        }
    }

    private void grabFacingTile() {
        List<Crate> touchedCrates = getTouching(Crate.class);
        if (touchedCrates.isEmpty()) return;
        Predicate<Crate> pred = it -> true;
        switch (dir) {
            case UP -> pred = it -> it.getY() - getY() == -16;
            case DOWN -> pred = it -> it.getY() - getY() == 16;
            case LEFT -> pred = it -> it.getX() - getX() == -16;
            case RIGHT -> pred = it -> it.getX() - getX() == 16;
        }
        touchedCrates.stream().filter(pred).findFirst().ifPresent(it -> {
            grabbed = it;
            it.grabbed = true;
        });
    }

    private void calculateMovement() {
        int dx = 0, dy = 0;

        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) dy -= speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) dy += speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) dx -= speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) dx += speed;

        if (grabbed == null) {
            if (dy < 0) dir = PlayerDirection.UP;
            else if (dy > 0) dir = PlayerDirection.DOWN;
            else if (dx < 0) dir = PlayerDirection.LEFT;
            else if (dx > 0) dir = PlayerDirection.RIGHT;
            texture = dir.texture;
        }

        if (dx != 0) {
            move(dx, 0);
            if (collidesWith(WallTile.class) || collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbed))
                move(-dx, 0);
            else if (grabbed != null) {
                grabbed.move(dx, 0);
                if (grabbed.collidesWith(WallTile.class)) {
                    move(-dx, 0);
                    grabbed.move(-dx, 0);
                }
            }
        }

        if (dy != 0) {
            move(0, dy);
            if (collidesWith(WallTile.class) || collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbed))
                move(0, -dy);
            else if (grabbed != null) {
                grabbed.move(0, dy);
                if (grabbed.collidesWith(WallTile.class)) {
                    move(0, -dy);
                    grabbed.move(0, -dy);
                }
            }
        }
    }

    private enum PlayerDirection {
        UP(Textures.PLAYER_UP), DOWN(Textures.PLAYER_DOWN), LEFT(Textures.PLAYER_LEFT), RIGHT(Textures.PLAYER_RIGHT);

        public final Textures texture;

        PlayerDirection(Textures texture) {
            this.texture = texture;
        }
    }
}
