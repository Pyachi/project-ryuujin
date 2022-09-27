package com.cs321.team1.framework.map;

public class Location {
    private int x;
    private int y;
    
    public static Location at(int x, int y) {
        return new Location().set(x, y);
    }
    
    public static Location atTile(int x, int y) {
        return new Location().setTile(x, y);
    }
    
    private Location() {
    }
    
    public int getX() {
        return x;
    }
    
    public Location setX(int x) {
        this.x = x;
        return this;
    }
    
    public int getY() {
        return y;
    }
    
    public Location setY(int y) {
        this.y = y;
        return this;
    }
    
    public Location set(int x, int y) {
        return setX(x).setY(y);
    }
    
    public int getTileX() {
        return getX() / 16;
    }
    
    public Location setTileX(int x) {
        return setX(x * 16);
    }
    
    public int getTileY() {
        return getY() / 16;
    }
    
    public Location setTileY(int y) {
        return setY(y * 16);
    }
    
    public Location setTile(int x, int y) {
        return setTileX(x).setTileY(y);
    }
    
    public Location centralize() {
        return set(getX() + 8, getY() + 8);
    }
    
    public Location move(int x, int y) {
        return set(getX() + x, getY() + y);
    }
    
    public Location moveTiles(int x, int y) {
        return set(getX() + x * 16, getY() + y * 16);
    }
    
    @Override
    public Location clone() {
        return Location.at(getX(), getY());
    }
    
    public Location getMidpoint(Location other) {
        return Location.at((getX() + other.getX()) / 2, (getY() + other.getY()) / 2);
    }
}
