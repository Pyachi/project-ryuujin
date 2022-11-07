package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.Vec2;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Abstract object defining in-level objects and behaviors
 */
public abstract class GameObject {
    private Level level = null;
    private Vec2 loc = new Vec2(1, 1).toTile();
    private Vec2 size = new Vec2(1, 1).toTile();
    private Texture texture = new Texture("null", -1);
    private int id;
    private boolean dead = false;
    private int tick = 0;
    
    /**
     * Checks if given location is contained within object
     *
     * @param location The location to check collision with
     * @return True if location is within object, false otherwise
     */
    public boolean collidesWith(Vec2 location) {
        return !isDead() && getLocation().x() < location.x() + 1 &&
                getLocation().x() + getSize().x() > location.x() - 1 && getLocation().y() < location.y() + 1 &&
                getLocation().y() + getSize().y() > location.y() - 1;
    }
    
    /**
     * Checks if two objects collide with each other
     *
     * @param other GameObject to check collision with
     * @return True if both objects collide, false otherwise
     */
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        return getLocation().x() < other.getLocation().x() + other.getSize().x() &&
                getLocation().x() + getSize().x() > other.getLocation().x() &&
                getLocation().y() < other.getLocation().y() + other.getSize().y() &&
                getLocation().y() + getSize().y() > other.getLocation().y();
    }
    
    /**
     * Checks if object collides with anything of given class
     *
     * @param clazz The class type to check collision with
     * @return True if object collides with other object of class type, false otherwise
     */
    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }
    
    /**
     * Gets a list of all objects colliding with this object
     *
     * @return List of all collided objects
     */
    public List<GameObject> getCollisions() {
        return getLevel().getObjects().stream().filter(this::collidesWith).toList();
    }
    
    /**
     * Gets a list of all objects of given type that collide with this object
     *
     * @param clazz The type of objects to get a list of
     * @param <T>   Generic for type casting
     * @return A list of all objects of type T colliding with this object
     */
    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    /**
     * Gets internal ID of object
     *
     * @return Internal ID of object
     */
    public int getID() {
        return id;
    }
    
    /**
     * Get level the object is in
     *
     * @return Level the object is in
     */
    public Level getLevel() {
        return level;
    }
    
    /**
     * Sets level of the object
     *
     * @param lvl Level the object is in
     */
    public void setLevel(Level lvl) {
        if (!dead) level = lvl;
    }
    
    /**
     * Gets location of the object
     *
     * @return Location of the object
     */
    public Vec2 getLocation() {
        return loc;
    }
    
    /**
     * Sets location of the object
     *
     * @param vec Location of the object
     */
    public void setLocation(Vec2 vec) {
        if (!dead) loc = vec;
    }
    
    /**
     * Gets the size of the object
     *
     * @return Size of the object
     */
    public Vec2 getSize() {
        return size;
    }
    
    /**
     * Sets the size of the object
     *
     * @param vec Size of the object
     */
    public void setSize(Vec2 vec) {
        if (!dead) size = vec;
    }
    
    /**
     * Gets the texture of the object
     *
     * @return Texture of the object
     */
    public Texture getTexture() {
        return texture;
    }
    
    /**
     * Sets the texture of the object
     *
     * @param tex Texture of the object
     */
    public void setTexture(Texture tex) {
        if (!dead) texture = tex;
    }
    
    /**
     * Increases lifetime of the object
     */
    public void incTick() {
        tick++;
    }
    
    /**
     * Checks if object is set for deletion or not
     *
     * @return True if object is scheduled for removal, false otherwise
     */
    public boolean isDead() {
        return dead;
    }
    
    /**
     * Schedules object for removal
     */
    public void kill() {
        if (level != null) getLevel().removeObject(this);
        texture = null;
        loc = null;
        size = null;
        dead = true;
    }
    
    /**
     * Moves object by (x,y) without any checks
     *
     * @param x left-right direction of movement
     * @param y up-down direction of movement
     */
    public void move(int x, int y) {
        if (!dead) setLocation(getLocation().add(new Vec2(x, y)));
    }
    
    /**
     * Handles rendering of object
     *
     * @param g Graphics item used to draw with
     */
    public void paint(Graphics2D g) {
        if (!dead && texture != null) getTexture().paint(this, g, tick);
    }
    
    /**
     * Sets the internal ID of object
     *
     * @param id ID to set object to
     */
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    abstract public String toString();
    
    /**
     * Gets lifetime of object
     *
     * @return lifetime of object
     */
    protected int getTick() {
        return tick;
    }
}
