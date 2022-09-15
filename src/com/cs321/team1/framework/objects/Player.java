package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Textures;
import com.cs321.team1.framework.objects.crates.Crate;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.util.Direction;
import com.cs321.team1.util.Keyboard;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Predicate;

public class Player extends GameObject implements Runnable {
    private Direction dir = Direction.NORTH;
    private Crate grabbed = null;
    
    public Player() {
        super(null);
        setTexture(Textures.PLAYER_UP);
        getLocation().setTile(10, 6);
    }
    
    @Override
    public Room getRoom() {
        return Game.getDungeon().getActiveRoom();
    }
    
    @Override
    public void run() {
        if (grabbed == null && Keyboard.isKeyPressed(KeyEvent.VK_SHIFT)) grabFacingTile();
        if (grabbed != null && (!Keyboard.isKeyPressed(KeyEvent.VK_SHIFT) || grabbed.isDead())) {
            grabbed.grabbed = false;
            grabbed = null;
        }
        calculateMovement();
    }
    
    private void calculateMovement() {
        int x = 0, y = 0;
        int speed = 2;
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) y -= speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) y += speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) x -= speed;
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) x += speed;
        if (grabbed == null) {
            if (y < 0) {
                dir = Direction.NORTH;
                setTexture(Textures.PLAYER_UP);
            } else if (y > 0) {
                dir = Direction.SOUTH;
                setTexture(Textures.PLAYER_DOWN);
            } else if (x < 0) {
                dir = Direction.WEST;
                setTexture(Textures.PLAYER_LEFT);
            } else if (x > 0) {
                dir = Direction.EAST;
                setTexture(Textures.PLAYER_RIGHT);
            }
        }
        move(x, y);
    }
    
    private void grabFacingTile() {
        List<Crate> touchedCrates = getTouching(Crate.class);
        if (touchedCrates.isEmpty()) return;
        Predicate<Crate> pred = it -> true;
        switch (dir) {
            case NORTH -> pred = it -> it.getLocation().getY() - getLocation().getY() <= -16;
            case SOUTH -> pred = it -> it.getLocation().getY() - getLocation().getY() >= 16;
            case WEST -> pred = it -> it.getLocation().getX() - getLocation().getX() <= -16;
            case EAST -> pred = it -> it.getLocation().getX() - getLocation().getX() >= 16;
        }
        touchedCrates.stream().filter(pred).findFirst().ifPresent(it -> {
            grabbed = it;
            it.grabbed = true;
        });
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = collidesWith(UnpassableTile.class) ||
                collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbed);
        super.move(-x, -y);
        return !collision;
    }
    
    @Override
    public void move(int x, int y) {
        if (grabbed == null) {
            if (canMove(x, 0)) super.move(x, 0);
            if (canMove(0, y)) super.move(0, y);
        } else {
            if (canMove(x, 0) && grabbed.canMove(x, 0)) {
                super.move(x, 0);
                grabbed.move(x, 0);
            }
            if (canMove(0, y) && grabbed.canMove(0, y)) {
                super.move(0, y);
                grabbed.move(0, y);
            }
        }
    }
}
