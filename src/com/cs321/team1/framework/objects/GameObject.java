package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

import java.awt.Graphics2D;
import java.util.List;

public abstract class GameObject {
    private final Room room;
    private Texture texture = new Texture(0, 0);
    private Location location = new Location(0, 0);
    private boolean dead;
    
    public GameObject(Room room) {
        this.room = room;
        if (room != null) room.addObject(this);
    }
    
    public Room getRoom() {
        return room;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void setTexture(Texture texture) {
        if (!dead) this.texture = texture;
    }
    
    public void setTexture(Textures texture) {
        if (!dead) this.texture = new Texture(texture);
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        if (!dead) this.location = location;
    }
    
    public void kill() {
        getRoom().removeObject(this);
        texture = null;
        location = null;
        dead = true;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void move(int x, int y) {
        if (!dead) getLocation().set(getLocation().getX() + x, getLocation().getY() + y);
    }
    
    public void moveTiles(int x, int y) {
        if (!dead) move(x * Game.tileSize, y & Game.tileSize);
    }
    
    public void paint(Graphics2D g) {
        getTexture().paint(getLocation(), g);
    }
    //******************************************************************************************************************
    //Collision handling
    
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || getRoom() != other.getRoom()) return false;
        return getLocation().getX() < other.getLocation().getX() + other.getTexture().getWidth() &&
                getLocation().getX() + getTexture().getWidth() > other.getLocation().getX() &&
                getLocation().getY() < other.getLocation().getY() + other.getTexture().getHeight() &&
                getLocation().getY() + getTexture().getHeight() > other.getLocation().getY();
    }
    
    public boolean collidesWith(Class<?> clazz) {
        return !getCollisions(clazz).isEmpty();
    }
    
    public List<GameObject> getCollisions() {
        return getRoom().getObjects().stream().filter(this::collidesWith).toList();
    }
    
    public <T> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    public boolean isTouching(GameObject other) {
        if (this == other || isDead() || getRoom() != other.getRoom()) return false;
        int locX = this.getLocation().getX() - 1;
        int locY = this.getLocation().getY() - 1;
        int width = this.getTexture().getWidth() + 2;
        int height = this.getTexture().getHeight() + 2;
        return locX < other.getLocation().getX() + other.getTexture().getWidth() &&
                locX + width > other.getLocation().getX() &&
                locY < other.getLocation().getY() + other.getTexture().getHeight() &&
                locY + height > other.getLocation().getY();
    }
    
    public boolean isTouching(Class<?> clazz) {
        return !getTouching(clazz).isEmpty();
    }
    
    public List<GameObject> getTouching() {
        return getRoom().getObjects().stream().filter(this::isTouching).toList();
    }
    
    public <T> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
}
