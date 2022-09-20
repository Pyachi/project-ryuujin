package com.cs321.team1.framework.textures;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;

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
        this.width = width * 16;
        this.height = height * 16;
        loadImage();
    }
    
    public Texture(Textures texture) {
        this.texture = texture;
        loadImage();
        calculateSize();
    }
    
    public Texture(int width, int height) {
        this.texture = Textures.NOTHING;
        this.width = width * 16;
        this.height = height * 16;
        loadImage();
    }
    
    public Texture() {
        this.texture = Textures.NOTHING;
        this.width = 16;
        this.height = 16;
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
    
    public void paint(GameObject obj, Graphics2D g) {
        if (!ignore) g.drawImage(
                image,
                (obj.getLocation().getX() - 16) * obj.getRoom().getScale(),
                (obj.getLocation().getY() - 16) * obj.getRoom().getScale(),
                width * obj.getRoom().getScale(),
                height * obj.getRoom().getScale(),
                null
        );
        if (texture.priority == -1) ignore = true;
    }
}
