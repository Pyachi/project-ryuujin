package com.cs321.team1.objects;

import com.cs321.team1.map.Level;
import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.Textures;

import java.awt.*;
import java.util.List;

public abstract class GameObject {
    private Level level = null;
    private Texture texture = Textures.NULL.get();
    private Location location = new Location(1, 1);
    private boolean dead = false;
    protected int tick = 0;
    
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
    
    public boolean isDead() {
        return dead;
    }
    
    public void paint(Graphics2D g) {
        if (!dead) getTexture().paint(this, g, tick);
    }
    //******************************************************************************************************************
    //Collision handling
    
    public boolean collidesWith(Location location) {
        return !isDead()
                && getLocation().x() < location.x() + 1
                && getLocation().x() + getTexture().getWidth() > location.x() - 1
                && getLocation().y() < location.y() + 1
                && getLocation().y() + getTexture().getHeight() > location.y() - 1;
    }
    
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        return getLocation().x() < other.getLocation().x() + other.getTexture().getWidth()
                && getLocation().x() + getTexture().getWidth() > other.getLocation().x()
                && getLocation().y() < other.getLocation().y() + other.getTexture().getHeight()
                && getLocation().y() + getTexture().getHeight() > other.getLocation().y();
    }
    
    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }
    
    public List<GameObject> getCollisions() {
        return getLevel().getObjects().stream().filter(this::collidesWith).toList();
    }
    
    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    public boolean isTouching(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        int locX = this.getLocation().x() - 1;
        int locY = this.getLocation().y() - 1;
        int width = this.getTexture().getWidth() + 2;
        int height = this.getTexture().getHeight() + 2;
        return locX < other.getLocation().x() + other.getTexture().getWidth()
                && locX + width > other.getLocation()
                                       .x()
                && locY < other.getLocation().y() + other.getTexture().getHeight()
                && locY + height > other.getLocation().y();
    }
    
    public boolean isTouching(Class<? extends GameObject> clazz) {
        return !getTouching(clazz).isEmpty();
    }
    
    public List<GameObject> getTouching() {
        return getLevel().getObjects().stream().filter(this::isTouching).toList();
    }
    
    public <T extends GameObject> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
}
