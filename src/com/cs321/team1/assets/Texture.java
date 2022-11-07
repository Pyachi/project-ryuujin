package com.cs321.team1.assets;

import com.cs321.team1.game.Game;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.GameObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * Class used to define sprite size, animation, and file location, and provide utilities for drawing sprites to the
 * screen
 */
public class Texture {
    private static final String TEXTURES_PATH = "resources/textures/";
    /**
     * Render priority of sprite; higher values render on top of lower values
     */
    public final int priority;
    /**
     * Frame count of animated sprites
     * Non-animated sprites are always set to 1
     */
    public final int frames;
    /**
     * Pixel size of one frame of sprite
     */
    public final Vec2 size;
    private final BufferedImage image;
    private final String path;
    
    /**
     * Creates a texture from image file
     *
     * @param path     Path to image file
     * @param priority Render priority of texture
     */
    public Texture(String path, int priority) {
        this.path = path;
        BufferedImage tempImage;
        try {
            tempImage = ImageIO.read(ResourceLoader.loadStream(TEXTURES_PATH + path + ".png"));
        } catch (Exception e) {
            try {
                tempImage = ImageIO.read(ResourceLoader.loadStream(TEXTURES_PATH + "null.png"));
                priority = 100;
            } catch (Exception ex) {
                //This should never happen, but hard-crash just in case
                System.exit(-1);
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
    
    /**
     * Deserializes texture data from save-file string
     *
     * @param tex Serialized texture data
     * @return Texture from serialized data
     */
    public static Texture fromString(String tex) {
        var args = tex.split(":");
        return new Texture(args[1], Integer.parseInt(args[0]));
    }
    
    /**
     * Fills entire screen with texture
     *
     * @param g    Graphics object to draw with
     * @param tick Given lifetime of screen (for animations)
     */
    public void fillCanvas(Graphics2D g, int tick) {
        if (image != null) g.drawImage(image.getSubimage(0, size.y() * ((tick / 5) % frames), size.x(), size.y()),
                0,
                0,
                Game.get().getRenderingManager().getScreenSize().x(),
                Game.get().getRenderingManager().getScreenSize().y(),
                null);
    }
    
    /**
     * Paints texture onto provided GameObject
     *
     * @param obj  Object to paint texture onto
     * @param g    Graphics object to draw with
     * @param tick Given lifetime of object (for animations)
     */
    public void paint(GameObject obj, Graphics2D g, int tick) {
        if (image != null) g.drawImage(image.getSubimage(0, size.y() * ((tick / 5) % frames), size.x(), size.y()),
                (obj.getLocation().x() - 16) * obj.getLevel().getScale(),
                (obj.getLocation().y() - 16) * obj.getLevel().getScale(),
                obj.getSize().x() * obj.getLevel().getScale(),
                obj.getSize().y() * obj.getLevel().getScale(),
                null);
    }
    
    /**
     * Serializes texture data for save-file usage
     *
     * @return Serialized texture data
     */
    @Override
    public String toString() {
        return priority + ":" + path;
    }
}
