package com.cs321.team1.assets;

import com.cs321.team1.game.Game;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.GameObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Texture {

  private static final String TEXTURES_PATH = "resources/textures/";
  public final int priority;
  public final int frames;
  public final Vec2 size;
  private final BufferedImage image;
  private final String path;

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
    int width = image.getWidth();
    frames = path.contains("animated") ? image.getHeight() / width : 1;
    int height = path.contains("animated") ? image.getHeight() / frames : image.getHeight();
    size = new Vec2(width, height);
  }

  public static Texture fromString(String tex) {
    String[] args = tex.split(":");
    return new Texture(args[1], Integer.parseInt(args[0]));
  }

  public void fillCanvas(Graphics2D g, int tick) {
    if (image != null) {
      g.drawImage(image.getSubimage(0, size.y * ((tick / 5) % frames), size.x, size.y), 0, 0,
          Game.get().getRenderingManager().getScreenSize().x,
          Game.get().getRenderingManager().getScreenSize().y, null);
    }
  }

  public void fillImage(BufferedImage image, int tick) {
    if (this.image != null) {
      image.createGraphics()
          .drawImage(this.image.getSubimage(0, size.y * ((tick / 5) % frames), size.x, size.y), 0,
              0, image.getWidth(), image.getHeight(), null);
    }
  }

  public void paint(GameObject obj, Graphics2D g, int tick) {
    if (image != null) {
      g.drawImage(image.getSubimage(0, size.y * ((tick / 5) % frames), size.x, size.y),
          (obj.getLocation().x - 16) * obj.getLevel().getScale(),
          (obj.getLocation().y - 16) * obj.getLevel().getScale(),
          obj.getSize().x * obj.getLevel().getScale(), obj.getSize().y * obj.getLevel().getScale(),
          null);
    }
  }

  @Override
  public String toString() {
    return priority + ":" + path;
  }
}
