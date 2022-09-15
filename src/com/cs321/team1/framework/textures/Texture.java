package com.cs321.team1.framework.textures;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.Player;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
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
    }
    
    public Texture(Textures texture) {
        this.texture = texture;
        calculateSize();
    }
    
    public Texture(int width, int height) {
        this.texture = Textures.NOTHING;
        this.width = width * Game.tileSize;
        this.height = height * Game.tileSize;
    }
    
    public Texture() {
        this.texture = Textures.NOTHING;
        this.width = Game.tileSize;
        this.height = Game.tileSize;
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
    
    private void calculateSize() {
        try {
            image = ImageIO.read(new File(texture.path));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            width = Game.tileSize;
            height = Game.tileSize;
        }
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
