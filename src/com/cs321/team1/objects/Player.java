package com.cs321.team1.objects;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.map.Location;
import com.cs321.team1.objects.crates.Crate;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.assets.Textures;

import java.util.Objects;

public class Player extends GameObject {
    private Direction dir = Direction.SOUTH;
    private Crate grabbedCrate = null;
    private boolean blocked = false;
    private int tryX = 0;
    private int tryY = 0;
    
    public Player(Location location) {
        setTexture(Textures.PLAYER_DOWN.get());
        setLocation(location);
    }
    
    public Crate getGrabbedCrate() {
        return grabbedCrate;
    }
    
    @Tick(priority = 0)
    public void movement() {
        blocked = false;
        tryX = 0;
        tryY = 0;
        handleCrates();
        calculateMovement();
    }
    
    @Tick(priority = 1)
    public void confirmMovement() {
        if (!blocked) return;
        blocked = false;
        int x = tryX;
        int y = tryY;
        tryX = 0;
        tryY = 0;
        move(x, 0);
        move(0, y);
    }
    
    @Tick(priority = 2)
    public void confirmMovementAgain() {
        if (!blocked) return;
        blocked = false;
        int x = tryX;
        int y = tryY;
        tryX = 0;
        tryY = 0;
        move(x, 0);
        move(0, y);
    }
    
    private void handleCrates() {
        if (grabbedCrate == null && Controls.GRAB.isHeld()) {
            getTouching(Crate.class).stream()
                                    .filter(it -> it.collidesWith(switch (dir) {
                                        case NORTH -> getLocation().add(8, 0);
                                        case SOUTH -> getLocation().add(8, 16);
                                        case WEST -> getLocation().add(0, 8);
                                        case EAST -> getLocation().add(16, 8);
                                    }) && it.canGrab() && getLevel().getObjects()
                                                                    .stream()
                                                                    .filter(Player.class::isInstance)
                                                                    .noneMatch(player -> ((Player) player).getGrabbedCrate()
                                                                            == it))
                                    .findFirst()
                                    .ifPresent(crate -> {
                                        Sounds.PICKUP.play();
                                        grabbedCrate = crate;
                                        updateCrateGraphics();
                                    });
        }
        if (grabbedCrate != null && (!Controls.GRAB.isHeld() || grabbedCrate.isDead())) {
            grabbedCrate = null;
            updateCrateGraphics();
        }
    }
    
    private void updateCrateGraphics() {
        var crates = getLevel().getObjects()
                               .stream()
                               .filter(Crate.class::isInstance)
                               .map(Crate.class::cast)
                               .filter(Crate::canGrab)
                               .toList();
        var grabbedCrates = getLevel().getObjects()
                                      .stream()
                                      .filter(Player.class::isInstance)
                                      .map(Player.class::cast)
                                      .map(Player::getGrabbedCrate)
                                      .filter(Objects::nonNull)
                                      .toList();
        crates.forEach(it -> it.setTexture(Textures.CRATE.get()));
        crates.stream()
              .filter(crate -> grabbedCrates.stream()
                                            .anyMatch(it -> it.canInteractWith(crate) || crate.canInteractWith(it)))
              .forEach(it -> it.setTexture(Textures.CRATE_INTERACTABLE.get()));
        grabbedCrates.forEach(it -> it.setTexture(Textures.CRATE_GRABBED.get()));
    }
    
    private void calculateMovement() {
        int x = 0, y = 0;
        if (Controls.UP.isHeld()) y -= 1;
        if (Controls.DOWN.isHeld()) y += 1;
        if (Controls.LEFT.isHeld()) x -= 1;
        if (Controls.RIGHT.isHeld()) x += 1;
        if (grabbedCrate == null) {
            if (y < 0) {
                dir = Direction.NORTH;
                setTexture(Textures.PLAYER_UP.get());
            } else if (y > 0) {
                dir = Direction.SOUTH;
                setTexture(Textures.PLAYER_DOWN.get());
            } else if (x < 0) {
                dir = Direction.WEST;
                setTexture(Textures.PLAYER_LEFT.get());
            } else if (x > 0) {
                dir = Direction.EAST;
                setTexture(Textures.PLAYER_RIGHT.get());
            }
        }
        for (int i = 0; i < 2; i++) move(x, y);
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = collidesWith(Player.class)
                || collidesWith(UnpassableTile.class)
                || collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbedCrate);
        super.move(-x, -y);
        return !collision;
    }
    
    public void move(int x, int y) {
        if (grabbedCrate == null) {
            if (canMove(x, 0)) super.move(x, 0);
            else {
                blocked = true;
                tryX += x;
            }
            if (canMove(0, y)) super.move(0, y);
            else {
                blocked = true;
                tryY += y;
            }
        } else {
            if (canMove(x, 0) && grabbedCrate.canMove(x, 0)) {
                super.move(x, 0);
                grabbedCrate.move(x, 0);
            } else {
                blocked = true;
                tryX += x;
            }
            if (canMove(0, y) && grabbedCrate.canMove(0, y)) {
                super.move(0, y);
                grabbedCrate.move(0, y);
            } else {
                blocked = true;
                tryY += y;
            }
        }
    }
    
    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
