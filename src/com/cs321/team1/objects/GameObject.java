package com.cs321.team1.objects;

import com.cs321.team1.map.Level;
import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;

import java.awt.*;
import java.util.List;

/**
 * Abstract base for every in-game entity
 */
public abstract class GameObject {
    private Level level = null;
    private Texture texture = null;
    private Location location = new Location(1, 1);
    private boolean dead = false;
    protected int tick = 0;
    private final int width;
    private final int height;
    
    protected GameObject(int tileWidth, int tileHeight) {
        width = tileWidth * 16;
        height = tileHeight * 16;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public void setLevel(Level level) {
        if (!dead) this.level = level;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void setTexture(Texture texture) {
        if (!dead) this.texture = texture;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        if (!dead) this.location = location;
    }
    
    public void move(int x, int y) {
        if (!dead) setLocation(getLocation().add(x, y));
    }
    
    public void kill() {
        if (level != null) getLevel().removeObject(this);
        texture = null;
        location = null;
        dead = true;
    }
    
    @Tick
    public void tick() {
        tick++;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void paint(Graphics2D g) {
        if (!dead && texture != null) getTexture().paint(this, g, tick);
    }
    //******************************************************************************************************************
    //Collision handling
    
    /**
     * Checks if object collides with point location
     *
     * @param location Chosen point location to check collision with
     * @return True if object collides with point, false otherwise
     */
    public boolean collidesWith(Location location) {
        return !isDead()
                && getLocation().x() < location.x() + 1
                && getLocation().x() + getWidth() > location.x() - 1
                && getLocation().y() < location.y() + 1
                && getLocation().y() + getHeight() > location.y() - 1;
    }
    
    /**
     * Checks if object collides with other object
     *
     * @param other Chosen object to check collision with
     * @return True if both objects collide, false otherwise
     */
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        return getLocation().x() < other.getLocation().x() + other.getWidth()
                && getLocation().x() + getWidth() > other.getLocation().x()
                && getLocation().y() < other.getLocation().y() + other.getHeight()
                && getLocation().y() + getHeight() > other.getLocation().y();
    }
    
    /**
     * Checks if object collides with any object of specified type
     *
     * @param clazz Chosen type of object to check collision with
     * @return True if object collides with specified type, false otherwise
     */
    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }
    
    /**
     * Creates a list of all objects that collide with this object
     *
     * @return List of collisions with this object
     */
    public List<GameObject> getCollisions() {
        return getLevel().getObjects().stream().filter(this::collidesWith).toList();
    }
    
    /**
     * Creates a list of all objects of specified type that collide with this object
     *
     * @param clazz Chosen type of object to check collision with
     * @param <T>   Generic used to automatically cast to given type
     * @return List of collisions with this object of given type
     */
    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    /**
     * Checks if object is physically touching other object
     * Objects that collide are always touching, but not vice-versa
     *
     * @param other Chosen object to check touching with
     * @return True if both objects touch, false otherwise
     */
    public boolean isTouching(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        int locX = getLocation().x() - 1;
        int locY = getLocation().y() - 1;
        int width = getWidth() + 2;
        int height = getHeight() + 2;
        return locX < other.getLocation().x() + other.getWidth()
                && locX + width > other.getLocation().x()
                && locY < other.getLocation().y() + other.getHeight()
                && locY + height > other.getLocation().y();
    }
    
    /**
     * Checks if object is physically touching any object of specified type
     *
     * @param clazz Chosen type to check touching with
     * @return True if object touches any object of specified type, false otherwise
     */
    public boolean isTouching(Class<? extends GameObject> clazz) {
        return !getTouching(clazz).isEmpty();
    }
    
    /**
     * Creates a list of all objects that touch this object
     *
     * @return List of touching objects
     */
    public List<GameObject> getTouching() {
        return getLevel().getObjects().stream().filter(this::isTouching).toList();
    }
    
    /**
     * Creates a list of all objects touching this object of specified type
     *
     * @param clazz Chosen type to get list of touching
     * @param <T>   Generic used to automatically cast to chosen type
     * @return List of touching objects of specified type
     */
    public <T extends GameObject> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    public boolean isInside(GameObject other) {
        return other.collidesWith(getLocation().add(width / 2, height / 2));
    }
    
    public boolean isInside(Class<? extends GameObject> clazz) {
        return !getInside(clazz).isEmpty();
    }
    
    public List<GameObject> getInside() {
        return getLevel().getObjects().stream().filter(it -> it.isInside(this)).toList();
    }
    
    public <T extends GameObject> List<T> getInside(Class<T> clazz) {
        return getInside().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
}
