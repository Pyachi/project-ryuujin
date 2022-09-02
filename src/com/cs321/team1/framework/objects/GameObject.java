package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class GameObject {
    private int locX;
    private int locY;
    private boolean update = true;

    public int getX() {
        return locX / Game.scale;
    }

    public int getY() {
        return locY / Game.scale;
    }

    public void setPosition(int locX, int locY) {
        this.locX = locX * Game.scale;
        this.locY = locY * Game.scale;
    }

    public void setTilePosition(int locX, int locY) {
        this.locX = locX * Game.tileSize;
        this.locY = locY * Game.tileSize;
    }

    public void move(int locX, int locY) {
        this.locX += locX * Game.scale;
        this.locY += locY * Game.scale;
    }

    public boolean collidesWith(GameObject other) {
        return Math.abs(locX - other.locX) < Game.tileSize && Math.abs(locY - other.locY) < Game.tileSize;
    }

    public int getDistanceSqr(GameObject other) {
        int x = getX() - other.getX();
        int y = getY() - other.getY();
        return x * x + y * y;
    }

    public void paint(Graphics2D g) {
        if (update) {
            try {
                g.drawImage(ImageIO.read(new File(getTexture().path)), locX, locY, Game.tileSize, Game.tileSize, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            update = false;
        }
    }

    public void update() {
        update = true;
    }

    abstract protected Textures getTexture();
}
