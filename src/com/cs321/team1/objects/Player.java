package com.cs321.team1.objects;

import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.crates.Crate;

import java.util.Objects;

public class Player extends GameObject {
    private Direction dir = Direction.SOUTH;
    private Crate grabbedCrate = null;
    private boolean blocked = false;
    private int tryX = 0;
    private int tryY = 0;
    
    public Player(Vec2 location) {
        setTexture("player/right");
        setSize(new Vec2(1, 1).toTile());
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
            getLevel().getObjects()
                      .stream()
                      .filter(Crate.class::isInstance)
                      .map(Crate.class::cast)
                      .filter(it -> it.collidesWith(switch (dir) {
                          case NORTH -> getLocation().add(new Vec2(8, 0));
                          case SOUTH -> getLocation().add(new Vec2(8, 16));
                          case WEST -> getLocation().add(new Vec2(0, 8));
                          case EAST -> getLocation().add(new Vec2(16, 8));
                      }) && it.canGrab() && getLevel().getObjects()
                                                      .stream()
                                                      .filter(Player.class::isInstance)
                                                      .noneMatch(player -> ((Player) player).getGrabbedCrate() == it))
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
        var crates = getLevel().getObjects().stream().filter(Crate.class::isInstance).map(Crate.class::cast).filter(
                Crate::canGrab).toList();
        var grabbedCrates = getLevel().getObjects()
                                      .stream()
                                      .filter(Player.class::isInstance)
                                      .map(Player.class::cast)
                                      .map(Player::getGrabbedCrate)
                                      .filter(Objects::nonNull)
                                      .toList();
        crates.forEach(it -> it.setTexture(new Texture("crates/crate", 1)));
        crates.stream()
              .filter(crate -> grabbedCrates.stream()
                                            .anyMatch(it -> it.canInteractWith(crate) || crate.canInteractWith(it)))
              .forEach(it -> it.setTexture(new Texture("crates/interactable", 1)));
        grabbedCrates.forEach(it -> it.setTexture(new Texture("crates/grabbed", 1)));
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
                setTexture("player/up_walk_animated");
            } else if (y > 0) {
                dir = Direction.SOUTH;
                setTexture("player/down_walk_animated");
            } else if (x < 0) {
                dir = Direction.WEST;
                setTexture("player/left_walk_animated");
            } else if (x > 0) {
                dir = Direction.EAST;
                setTexture("player/right_walk_animated");
            } else {
                switch (dir) {
                    case NORTH -> setTexture("player/up_idle_animated");
                    case SOUTH -> setTexture("player/down_idle_animated");
                    case EAST -> setTexture("player/right_idle_animated");
                    case WEST -> setTexture("player/left_idle_animated");
                }
            }
        }
        move(x, y);
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = collidesWith(Player.class) || collidesWith(UnpassableTile.class) ||
                collidesWith(Crate.class) && getCollisions(Crate.class).stream().anyMatch(it -> it != grabbedCrate);
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
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
    
    private void setTexture(String path) {
        setTexture(new Texture(path, 2));
    }
    
    @Override
    public String toString() {
        return "PLR|" + getLocation().toString();
    }
}
