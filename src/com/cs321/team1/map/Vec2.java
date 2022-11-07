package com.cs321.team1.map;

import java.awt.Dimension;

/**
 * Minimalistic record for a 2D Vector
 *
 * @param x left-right position, width, anything really
 * @param y up-down position, height, anything really
 */
public record Vec2(int x, int y) {
    /**
     * Converts a Dimension object to an equivalent Vec2
     *
     * @param dim The Dimension object to convert to Vec2
     * @return A Vec2 with the exact same size
     */
    public static Vec2 fromDimension(Dimension dim) {
        return new Vec2(dim.width, dim.height);
    }
    
    /**
     * Converts a serialized Vec2 string back into a Vec2
     *
     * @param str The Vec2 string to deserialize
     * @return A Vec2 with the specified size
     */
    public static Vec2 fromString(String str) {
        var args = str.split(":");
        var vec2 = new Vec2(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return args[0].equals("T") ? vec2.toTile() : vec2;
    }
    
    /**
     * Creates a new Vec2 with the combined values of the two inputs
     *
     * @param other The other Vec2 to add to this one
     * @return A Vec2 with the combined values of both Vec2s
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }
    
    /**
     * Converts Vec2 to a Dimension object
     *
     * @return A Dimension object with the same size
     */
    public Dimension toDimension() {
        return new Dimension(x, y);
    }
    
    @Override
    public String toString() {
        return (x % 16 == 0 && y % 16 == 0) ? "T:" + x / 16 + ":" + y / 16 : "R:" + x + ":" + y;
    }
    
    /**
     * Converts Vec2 to a location of indexed tile
     *
     * @return The Tiled Vec2
     */
    public Vec2 toTile() {
        return new Vec2(x * 16, y * 16);
    }
}
