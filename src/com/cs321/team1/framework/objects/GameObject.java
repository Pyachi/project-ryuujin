package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class GameObject {
    //Location of object in raw pixels
    private int locX;
    private int locY;
    //Render priority of object
    protected int locZ;
    protected Textures texture;

    //******************************************************************************************************************
    //Getters
    public int getX() {
        return locX / Game.scale;
    }

    public int getY() {
        return locY / Game.scale;
    }

    public int getZ() {
        return locZ;
    }

    public int getTileX() {
        return locX / Game.tileSize;
    }

    public int getTileY() {
        return locY / Game.tileSize;
    }

    //******************************************************************************************************************
    //Movement methods
    public void setPosition(int locX, int locY) {
        update();
        this.locX = locX * Game.scale;
        this.locY = locY * Game.scale;
        update();
    }

    public void setTilePosition(int locX, int locY) {
        update();
        this.locX = locX * Game.tileSize;
        this.locY = locY * Game.tileSize;
        update();
    }

    public void move(int locX, int locY) {
        setPosition(getX() + locX, getY() + locY);
    }

    //******************************************************************************************************************
    //Collision methods
    public boolean collidesWith(GameObject other) {
        return this != other && Math.abs(locX - other.locX) < Game.tileSize && Math.abs(locY - other.locY) < Game.tileSize;
    }

    public boolean collidesWith(Class<?> clazz) {
        return !getCollisions(clazz).isEmpty();
    }

    public List<GameObject> getCollisions() {
        return Game.getObjects().stream().filter(this::collidesWith).toList();
    }

    public <T> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    public boolean isTouching(GameObject other) {
        return this != other && Math.abs(locX - other.locX) < Game.tileSize + Game.scale && Math.abs(locY - other.locY) < Game.tileSize + Game.scale;
    }

    public boolean isTouching(Class<?> clazz) {
        return !getTouching(clazz).isEmpty();
    }

    public List<GameObject> getTouching() {
        return Game.getObjects().stream().filter(this::isTouching).toList();
    }

    public <T> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    //******************************************************************************************************************
    //Framework
    private boolean update;
    private boolean killed;

    public void paint(Graphics2D g) {
        if (update) {
            try {
                g.drawImage(ImageIO.read(new File(texture.path)), locX, locY, Game.tileSize, Game.tileSize, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            update = false;
        }
    }

    public void cleanup() {
        if (!killed) {
            killed = true;
            Game.removeObject(this);
        }
    }

    public boolean isKilled() {
        return killed;
    }

    public void update() {
        if (!update) {
            update = true;
            getCollisions().forEach(GameObject::update);
        }
    }
}
