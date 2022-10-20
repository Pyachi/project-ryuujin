package com.cs321.team1.map;

public record Vec2(int x, int y) {
    
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }
    
    public Vec2 toTile() {
        return new Vec2(x * 16, y * 16);
    }
    
    @Override
    public String toString() {
        return (x % 16 == 0 && y % 16 == 0) ? "T:" + x / 16 + ":" + y / 16 : "R:" + x + ":" + y;
    }
    
    public static Vec2 fromString(String vec) {
        var args = vec.split(":");
        var vec2 = new Vec2(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return args[0].equals("T") ? vec2.toTile() : vec2;
    }
}
