package com.cs321.team1.assets;

import com.cs321.team1.objects.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Texture {
    private final Textures texture;
    private final int width;
    private final int height;
    private BufferedImage image;
    private final int imageWidth;
    private final int imageHeight;
    private final int imageFrames;
    
    Texture(Textures texture, int frames, int width, int height) {
        this.texture = texture;
        imageFrames = frames;
        loadImage();
        imageWidth = image.getWidth();
        imageHeight = image.getHeight() / frames;
        this.width = width * 16;
        this.height = height * 16;
    }
    
    Texture(Textures texture, int frames) {
        this.texture = texture;
        imageFrames = frames;
        loadImage();
        imageWidth = image.getWidth();
        imageHeight = image.getHeight() / frames;
        width = imageWidth;
        height = imageHeight;
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
    
    public void paint(GameObject obj, Graphics2D g, int tick) {
        if (texture != Textures.NULL)
            g.drawImage(image.getSubimage(0, imageHeight * ((tick / 5) % imageFrames), imageWidth, imageHeight),
                        (obj.getLocation().x() - 16) * obj.getLevel().getScale(),
                        (obj.getLocation().y() - 16) * obj.getLevel().getScale(),
                        width * obj.getLevel().getScale(),
                        height * obj.getLevel().getScale(),
                        null);
    }
}
