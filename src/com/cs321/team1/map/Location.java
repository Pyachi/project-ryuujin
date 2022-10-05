package com.cs321.team1.map;

/**
 * An immutable class representing a two-dimensional position
 * Any modification of a location simply returns a new location
 * @param x Horizontal position from left to right in 1/16ths of a tile
 * @param y Vertical position from top to bottom in 1/16ths of a tile
 */
public record Location(int x, int y) {
    /**
     * Creates a tile-aligned location
     * @param x Horizontal position from left to right in tiles
     * @param y Vertical position from top to bottom in tiles
     * @return A location mapped to the tile coordinates specified
     */
    public static Location Tile(int x, int y) {
        return new Location(x * 16, y * 16);
    }
    
    /**
     * Creates a location relative to the existing location
     * @param x Horizontal displacement of location, positive values move right
     * @param y Vertical displacement of location, positive values move down
     * @return A location the provided distance from the existing location
     */
    public Location add(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }
}
