package com.cs321.team1.framework.textures;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
    private final Textures texture;
    private BufferedImage image;
    private int width;
    private int height;
    private boolean ignore;
    
    public Texture(Textures texture, int width, int height) {
        this.texture = texture;
        this.width = width * Game.tileSize;
        this.height = height * Game.tileSize;
        loadImage();
    }
    
    public Texture(Textures texture) {
        this.texture = texture;
        loadImage();
        calculateSize();
    }
    
    public Texture(int width, int height) {
        this.texture = Textures.NOTHING;
        this.width = width * Game.tileSize;
        this.height = height * Game.tileSize;
        loadImage();
    }
    
    public Texture() {
        this.texture = Textures.NOTHING;
        this.width = Game.tileSize;
        this.height = Game.tileSize;
        loadImage();
    }
    
    public Textures getTexture() {
        return texture;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    private void loadImage() {
        try {
            image = ImageIO.read(texture.path);
        } catch (IOException e) {
            System.out.println(texture.path);
            throw new RuntimeException(e);
        }
    }
    
    private void calculateSize() {
        width = image.getWidth();
        height = image.getHeight();
    }
    
    public void reset() {
        ignore = false;
    }
    
    public void paint(Location loc, Graphics2D g) {
        if (!ignore) g.drawImage(image,
                loc.getX() * Game.scale,
                loc.getY() * Game.scale,
                width * Game.scale,
                height * Game.scale,
                null
        );
        if (texture.priority == -1) ignore = true;
    }
}
