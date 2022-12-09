package com.cs321.team1.util;

import com.cs321.team1.obj.GameObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.imageio.ImageIO;

public class Texture {

  private static final String TEXTURES_PATH = "resources/textures/";
  public final int priority;
  public final int frames;
  public final Vec2 size;
  public final String path;
  public final BufferedImage buffer;

  public Texture(String path, int priority) {
    this.path = path;
    BufferedImage buffer;
    try {
      buffer = ImageIO.read(ResourceUtil.loadStream(TEXTURES_PATH + path + ".png"));
    } catch (Exception e) {
      try {
        buffer = ImageIO.read(ResourceUtil.loadStream(TEXTURES_PATH + "null.png"));
        priority = 100;
      } catch (Exception ex) {
        //This should never happen, but hard-crash just in case
        System.exit(-1);
        throw new RuntimeException(ex);
      }
    }
    this.priority = priority;
    this.buffer = buffer;
    int width = buffer.getWidth();
    frames = path.contains("animated") ? buffer.getHeight() / width : 1;
    int height = path.contains("animated") ? buffer.getHeight() / frames : buffer.getHeight();
    size = new Vec2(width, height);
  }

  public static Texture fromString(String tex) {
    String[] args = tex.split(":");
    return new Texture(args[1], Integer.parseInt(args[0]));
  }

  public void fillImage(VolatileImage image, int tick) {
    if (this.buffer != null) {
      image.createGraphics().drawImage(
          this.buffer.getSubimage(0, size.y() * ((tick / 5) % frames), size.x(), size.y()), 0, 0,
          image.getWidth(), image.getHeight(), null);
    }
  }

  public void paint(GameObject obj, Graphics2D buffer, int tick) {
    if (this.buffer != null) {
      buffer.drawImage(
          this.buffer.getSubimage(0, size.y() * ((tick / 5) % frames), size.x(), size.y()),
          (obj.getLocation().x() - 16) * obj.getLevel().getScale(),
          (obj.getLocation().y() - 16) * obj.getLevel().getScale(),
          obj.getSize().x() * obj.getLevel().getScale(),
          obj.getSize().y() * obj.getLevel().getScale(), null);
    }
  }

  @Override
  public String toString() {
    return priority + ":" + path;
  }
}
