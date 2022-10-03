package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

import java.awt.*;
import java.util.List;

public abstract class GameObject {
    private final Level level;
    private Texture texture;
    private Location location = Location.at(0, 0);
    private boolean dead;

    public GameObject(Level level) {
        this.level = level;
        if (level != null) level.addObject(this);
    }

    public Level getRoom() {
        return level;
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

    public void paint(Graphics2D g) {
        if (!dead) getTexture().paint(this, g);
    }
    //******************************************************************************************************************
    //Collision handling

    public boolean collidesWith(Location location) {
        return !isDead() && getLocation().getX() < location.getX() + 1 &&
                getLocation().getX() + getTexture().getWidth() > location.getX() - 1 &&
                getLocation().getY() < location.getY() + 1 &&
                getLocation().getY() + getTexture().getHeight() > location.getY() - 1;
    }

    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getRoom() != other.getRoom()) return false;
        return getLocation().getX() < other.getLocation().getX() + other.getTexture().getWidth() &&
                getLocation().getX() + getTexture().getWidth() > other.getLocation().getX() &&
                getLocation().getY() < other.getLocation().getY() + other.getTexture().getHeight() &&
                getLocation().getY() + getTexture().getHeight() > other.getLocation().getY();
    }

    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }

    public List<GameObject> getCollisions() {
        return getRoom().getObjects().stream().filter(this::collidesWith).toList();
    }

    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    public boolean isTouching(GameObject other) {
        if (this == other || isDead() || other.isDead() || getRoom() != other.getRoom()) return false;
        int locX = this.getLocation().getX() - 1;
        int locY = this.getLocation().getY() - 1;
        int width = this.getTexture().getWidth() + 2;
        int height = this.getTexture().getHeight() + 2;
        return locX < other.getLocation().getX() + other.getTexture().getWidth() &&
                locX + width > other.getLocation().getX() &&
                locY < other.getLocation().getY() + other.getTexture().getHeight() &&
                locY + height > other.getLocation().getY();
    }

    public boolean isTouching(Class<? extends GameObject> clazz) {
        return !getTouching(clazz).isEmpty();
    }

    public List<GameObject> getTouching() {
        return getRoom().getObjects().stream().filter(this::isTouching).toList();
    }

    public <T extends GameObject> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
}
