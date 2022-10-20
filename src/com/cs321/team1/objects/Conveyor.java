package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;
import com.cs321.team1.objects.crates.Crate;

public class Conveyor extends GameObject {
    private final int x;
    private final int y;

    public static Conveyor UP(Location loc) {
        return new Conveyor(loc, 0, -1);
    }

    public static Conveyor DOWN(Location loc) {
        return new Conveyor(loc, 0, 1);
    }

    public static Conveyor LEFT(Location loc) {
        return new Conveyor(loc, -1, 0);
    }

    public static Conveyor RIGHT(Location loc) {
        return new Conveyor(loc, 1, 0);
    }

    private Conveyor(Location loc, int x, int y) {
        this.x = x;
        this.y = y;
        setLocation(loc);
        if (y == -1) setTexture("map/conveyor_up_animated");
        else if (y == 1) setTexture("map/conveyor_down_animated");
        else if (x == -1) setTexture("map/conveyor_left_animated");
        else if (x == 1) setTexture("map/conveyor_right_animated");
    }

    @Tick(priority = 3)
    public void move() {
        getCollisions(Player.class).forEach(it -> {
            if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y)) it.move(x, y);
        });

        getCollisions(Crate.class).forEach(it -> {
            if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y) && !it.isGrabbed()) it.move(x, y);
        });
    }

    @Override
    public String toString() {
        var type = switch (x) {
            case -1 -> "L";
            case 1 -> "R";
            default -> switch (y) {
                case -1 -> "U";
                case 1 -> "D";
                default -> "X";
            };
        };
        return "CVR|" + getLocation().toString() + "|" + type;
    }

    private void setTexture(String path) {
        setTexture(new Texture(path, 0));
    }
}
