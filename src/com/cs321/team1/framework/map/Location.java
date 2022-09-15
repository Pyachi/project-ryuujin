package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Game;

public class Location {
    private int x;
    private int y;
    
    public static Location fromTile(int x, int y) {
        return new Location(x * Game.tileSize, y * Game.tileSize);
    }
    
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getTileX() {
        return x / Game.tileSize;
    }
    
    public void setTileX(int x) {
        this.x = x * Game.tileSize;
    }
    
    public int getTileY() {
        return y / Game.tileSize;
    }
    
    public void setTileY(int y) {
        this.y = y * Game.tileSize;
    }
    
    public void setTile(int x, int y) {
        this.x = x * Game.tileSize;
        this.y = y * Game.tileSize;
    }
    
    @Override
    public Location clone() {
        return new Location(getX(), getY());
    }
    
    public Location getMidpoint(Location other) {
        return new Location((getX() + other.getX()) / 2, (getY() + other.getY()) / 2);
    }
}
