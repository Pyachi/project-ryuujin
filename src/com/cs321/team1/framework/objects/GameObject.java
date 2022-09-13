package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class GameObject {
    //TODO add animation support
    protected Textures texture;
    //******************************************************************************************************************
    //Position handling
    
    private int locX;
    private int locY;
    private int width;
    private int height;
    
    public int getX() {
        return locX;
    }
    
    public int getY() {
        return locY;
    }
    
    public int getTileX() {
        return locX / Game.tileSize;
    }
    
    public int getTileY() {
        return locY / Game.tileSize;
    }
    
    public void setPosition(int x, int y) {
        locX = x;
        locY = y;
    }
    
    public void setTilePosition(int x, int y) {
        locX = x * Game.tileSize;
        locY = y * Game.tileSize;
    }
    
    public void move(int x, int y) {
        setPosition(locX + x, locY + y);
    }
    //******************************************************************************************************************
    //Collision handling
    
    public boolean collidesWith(GameObject other) {
        if (this == other) return false;
        return locX < other.locX + other.width && locX + width > other.locX && locY < other.locY + other.height && locY + height > other.locY;
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
        if (this == other) return false;
        int locX = this.locX - 1;
        int locY = this.locY - 1;
        int width = this.width + 2;
        int height = this.height + 2;
        return locX < other.locX + other.width && locX + width > other.locX && locY < other.locY + other.height && locY + height > other.locY;
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
    
    private boolean dead;
    public final int renderPriority;
    
    public GameObject(int renderPriority) {
        this.renderPriority = renderPriority;
    }
    
    public void kill() {
        dead = true;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public final void paint(Graphics2D g) {
        try {
            BufferedImage image = ImageIO.read(new File(texture.path));
            width = image.getWidth();
            height = image.getHeight();
            g.drawImage(
                    ImageIO.read(new File(texture.path)),
                    locX * Game.scale,
                    locY * Game.scale,
                    width * Game.scale,
                    height * Game.scale,
                    null
            );
        } catch (IOException ignored) {
        }
    }
}
