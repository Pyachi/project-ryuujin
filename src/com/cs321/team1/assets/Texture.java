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
    
    public Texture(String path, int priority) {
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
        width = image.getWidth();
        frames = path.contains("animated") ? image.getHeight() / width : 1;
        height = path.contains("animated") ? image.getHeight() / frames : image.getHeight();
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
