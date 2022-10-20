package com.cs321.team1.assets;

import com.cs321.team1.GameObject;
import com.cs321.team1.map.Vec2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
    private static final String TEXTURES_PATH = "src/resources/textures/";
    private final BufferedImage image;
    private final String path;
    
    public final int priority;
    public final int frames;
    public final Vec2 size;
    
    @Override
    public String toString() {
        return priority + ":" + path;
    }
    
    public static Texture fromString(String tex) {
        var args = tex.split(":");
        return new Texture(args[1], Integer.parseInt(args[0]));
    }
    
    public Texture(String path, int priority) {
        this.path = path;
        BufferedImage tempImage;
        try {
            tempImage = ImageIO.read(new File(TEXTURES_PATH + path + ".png"));
        } catch (IOException e) {
            try {
                tempImage = ImageIO.read(new File(TEXTURES_PATH + "null.png"));
                priority = 100;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        this.priority = priority;
        image = tempImage;
        var width = image.getWidth();
        frames = path.contains("animated") ? image.getHeight() / width : 1;
        var height = path.contains("animated") ? image.getHeight() / frames : image.getHeight();
        size = new Vec2(width, height);
    }
    
    public void paint(GameObject obj, Graphics2D g, int tick) {
        if (image != null) g.drawImage(image.getSubimage(0, size.y() * ((tick / 5) % frames), size.x(), size.y()),
                                       (obj.getLocation().x() - 16) * obj.getLevel().getScale(),
                                       (obj.getLocation().y() - 16) * obj.getLevel().getScale(),
                                       obj.getSize().x() * obj.getLevel().getScale(),
                                       obj.getSize().y() * obj.getLevel().getScale(),
                                       null);
    }
}
