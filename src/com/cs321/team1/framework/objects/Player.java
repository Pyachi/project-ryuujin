package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.objects.crates.Crate;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.framework.textures.Textures;
import com.cs321.team1.util.Direction;
import com.cs321.team1.util.Keyboard;

import java.awt.event.KeyEvent;

public class Player extends GameObject implements Runnable {
    public Direction dir = Direction.SOUTH;
    private Crate grabbedCrate = null;
    
    public Player(Level level, Location location) {
        super(level);
        setTexture(Textures.PLAYER_DOWN);
        setLocation(location);
    }
    
    public Crate getGrabbedCrate() {
        return grabbedCrate;
    }
    
    @Override
    public void run() {
        handleCrates();
        calculateMovement();
    }
    
    private void handleCrates() {
        if (grabbedCrate == null && Keyboard.isKeyHeld(KeyEvent.VK_SHIFT)) {
            getTouching(Crate.class).stream().filter(it -> it.collidesWith(switch (dir) {
                case NORTH -> getLocation().clone().centralize().move(0, -8);
                case SOUTH -> getLocation().clone().centralize().move(0, 8);
                case WEST -> getLocation().clone().centralize().move(-8, 0);
                case EAST -> getLocation().clone().centralize().move(8, 0);
            })).findFirst().ifPresent(crate -> {
                Sounds.PICKUP.play();
                grabbedCrate = crate;
                getRoom().getObjects()
                        .stream()
                        .filter(it -> it instanceof Crate &&
                                (((Crate) it).canInteractWith(crate) || crate.canInteractWith(((Crate) it))))
                        .forEach(it -> it.setTexture(Textures.CRATE_INTERACTABLE));
                crate.setTexture(Textures.CRATE_GRABBED);
            });
        }
        if (grabbedCrate != null && (!Keyboard.isKeyHeld(KeyEvent.VK_SHIFT) || grabbedCrate.isDead())) {
            grabbedCrate = null;
            getRoom().getObjects()
                    .stream()
                    .filter(Crate.class::isInstance)
                    .forEach(it -> it.setTexture(Textures.CRATE));
        }
    }
    
    private void calculateMovement() {
        int x = 0, y = 0;
        if (Keyboard.isKeyHeld(KeyEvent.VK_W)) y -= 1;
        if (Keyboard.isKeyHeld(KeyEvent.VK_S)) y += 1;
        if (Keyboard.isKeyHeld(KeyEvent.VK_A)) x -= 1;
        if (Keyboard.isKeyHeld(KeyEvent.VK_D)) x += 1;
        if (grabbedCrate == null) {
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
        for (int i = 0; i < 2; i++) move(x, y);
    }
    
    public boolean canMove(int x, int y) {
        getLocation().move(x, y);
        boolean collision = collidesWith(UnpassableTile.class) ||
                collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbedCrate);
        getLocation().move(-x, -y);
        return !collision;
    }
    
    public void move(int x, int y) {
        if (grabbedCrate == null) {
            if (canMove(x, 0)) getLocation().move(x, 0);
            if (canMove(0, y)) getLocation().move(0, y);
        } else {
            if (canMove(x, 0) && grabbedCrate.canMove(x, 0)) {
                getLocation().move(x, 0);
                grabbedCrate.getLocation().move(x, 0);
            }
            if (canMove(0, y) && grabbedCrate.canMove(0, y)) {
                getLocation().move(0, y);
                grabbedCrate.getLocation().move(0, y);
            }
        }
    }
}
