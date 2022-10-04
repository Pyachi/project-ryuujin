package com.cs321.team1.framework.textures;

import com.cs321.team1.framework.objects.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Texture {
    private final Textures texture;
    private final int width;
    private final int height;
    private BufferedImage image;
    private boolean ignore;
    
    Texture(Textures texture, int width, int height) {
        this.texture = texture;
        this.width = width * 16;
        this.height = height * 16;
        loadImage();
    }
    
    Texture(Textures texture) {
        this.texture = texture;
        loadImage();
        width = image.getWidth();
        height = image.getHeight();
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
            e.printStackTrace();
        }
    }
    
    public void reset() {
        ignore = false;
    }
    
    public void paint(GameObject obj, Graphics2D g) {
        if (!ignore) g.drawImage(image,
                                 (obj.getLocation().getX() - 16) * obj.getLevel().getScale(),
                                 (obj.getLocation().getY() - 16) * obj.getLevel().getScale(),
                                 width * obj.getLevel().getScale(),
                                 height * obj.getLevel().getScale(),
                                 null);
        if (texture.priority == -1) ignore = true;
    }
}
