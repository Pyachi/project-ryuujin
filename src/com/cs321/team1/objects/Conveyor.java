package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.crates.Crate;

/**
 * Object that moves crates and players in a given direction
 */
public class Conveyor extends GameObject {
    private final int x;
    private final int y;
    
    private Conveyor(Vec2 loc, int x, int y) {
        this.x = x;
        this.y = y;
        setLocation(loc);
        if (y == -1) setTexture("map/conveyor_up_animated");
        else if (y == 1) setTexture("map/conveyor_down_animated");
        else if (x == -1) setTexture("map/conveyor_left_animated");
        else if (x == 1) setTexture("map/conveyor_right_animated");
    }
    
    /**
     * Creates a conveyor facing downwards
     *
     * @param loc The location of the conveyor
     * @return A downwards conveyor at the given location
     */
    public static Conveyor DOWN(Vec2 loc) {
        return new Conveyor(loc, 0, 1);
    }
    
    /**
     * Creates a conveyor facing leftwards
     *
     * @param loc The location of the conveyor
     * @return A leftwards conveyor at the given location
     */
    public static Conveyor LEFT(Vec2 loc) {
        return new Conveyor(loc, -1, 0);
    }
    
    /**
     * Creates a conveyor facing rightwards
     *
     * @param loc The location of the conveyor
     * @return A rightwards conveyor at the given location
     */
    public static Conveyor RIGHT(Vec2 loc) {
        return new Conveyor(loc, 1, 0);
    }
    
    /**
     * Creates a conveyor facing upwards
     *
     * @param loc The location of the conveyor
     * @return A upwards conveyor at the given location
     */
    public static Conveyor UP(Vec2 loc) {
        return new Conveyor(loc, 0, -1);
    }
    
    /**
     * Handles movement of players and crates
     */
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
