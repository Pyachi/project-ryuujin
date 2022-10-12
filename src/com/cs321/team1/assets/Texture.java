package com.cs321.team1.assets;

import com.cs321.team1.objects.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Texture {
    private static final String TEXTURES_PATH = "src/resources/textures/";
    private final BufferedImage image;
    
    public final int priority;
    public final int frames;
    public final int width;
    public final int height;
    
    public static Texture Basic(String path, int priority) {
        return new Texture(path, priority, false);
    }
    
    public static Texture Animated(String path, int priority) {
        return new Texture(path, priority, true);
    }
    
    private Texture(String path, int priority, boolean animated) {
        this.priority = priority;
        try {
            image = ImageIO.read(new File(TEXTURES_PATH + path + ".png"));
            width = image.getWidth();
            height = animated ? image.getHeight() / width : image.getHeight();
            frames = animated ? image.getHeight() / image.getWidth() : 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void paint(GameObject obj, Graphics2D g, int tick) {
        if (image != null) g.drawImage(image.getSubimage(0, height * ((tick / 5) % frames), width, height),
                                       (obj.getLocation().x() - 16) * obj.getLevel().getScale(),
                                       (obj.getLocation().y() - 16) * obj.getLevel().getScale(),
                                       obj.getWidth() * obj.getLevel().getScale(),
                                       obj.getHeight() * obj.getLevel().getScale(),
                                       null);
    }
}
