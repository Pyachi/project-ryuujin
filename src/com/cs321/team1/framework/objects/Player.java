package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.crates.Crate;
import com.cs321.team1.framework.objects.intr.Tick;
import com.cs321.team1.framework.objects.intr.Tickable;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.framework.textures.Textures;

public class Player extends GameObject implements Tickable {
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

    @Tick(priority = 3)
    public void baseTick() {
        handleCrates();
        calculateMovement();
    }

    @Tick(priority = 2)
    public void checkCollision() {

    }

    private void handleCrates() {
        if (grabbedCrate == null && Controls.GRAB.isHeld()) {
            getTouching(Crate.class).stream().filter(it -> it.collidesWith(switch (dir) {
                case NORTH -> getLocation().clone().centralize().move(0, -8);
                case SOUTH -> getLocation().clone().centralize().move(0, 8);
                case WEST -> getLocation().clone().centralize().move(-8, 0);
                case EAST -> getLocation().clone().centralize().move(8, 0);
            }) && getRoom().getObjects().stream().filter(Player.class::isInstance).noneMatch(player -> ((Player) player).getGrabbedCrate() == it)).findFirst().ifPresent(crate -> {
                Sounds.PICKUP.play();
                grabbedCrate = crate;
            });
        }
        if (grabbedCrate != null && (!Controls.GRAB.isHeld() || grabbedCrate.isDead())) {
            grabbedCrate = null;
        }
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
        boolean collision = collidesWith(Player.class) || collidesWith(UnpassableTile.class) ||
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

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
