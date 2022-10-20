package com.cs321.team1.map;

/**
 * An immutable class representing a two-dimensional rectangle
 *
 * @param w Horizontal size in 1/16ths of a tile
 * @param h Vertical size in 1/16ths of a tile
 */
public record Dimension(int w, int h) {
    /**
     * Creates a tile-scaled dimension
     *
     * @param w Horizontal size in tiles
     * @param h Vertical size in tiles
     * @return A dimension of specified size
     */
    public static Dimension Tile(int w, int h) {
        return new Dimension(w * 16, h * 16);
    }

    @Override
    public String toString() {
        if (w % 16 == 0 && h % 16 == 0)
            return "T:" + w / 16 + ":" + h / 16;
        else return "R:" + w + ":" + h;
    }

    public static Dimension fromString(String loc) {
        var args = loc.split(":");
        if (args[0].equals("T")) return Dimension.Tile(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        else return new Dimension(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }
}