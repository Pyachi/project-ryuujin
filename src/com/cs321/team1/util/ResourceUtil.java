package com.cs321.team1.util;

import com.cs321.team1.game.Game;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class ResourceUtil {

  public static InputStream loadStream(String path) throws NullPointerException {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new NullPointerException("Input stream cannot be found");
    }
    return new BufferedInputStream(stream);
  }

  public static VolatileImage createImage() {
    Vec2 screenSize = Game.get().settings.getScreenSize();
    VolatileImage image = Game.get().renderer.getGraphicsConfiguration()
        .createCompatibleVolatileImage(screenSize.x(), screenSize.y(), Transparency.TRANSLUCENT);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.BLACK);
    graphics.setComposite(AlphaComposite.Clear);
    graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    graphics.dispose();
    return image;
  }

  public static VolatileImage createImage(int x, int y) {
    VolatileImage image = Game.get().renderer.getGraphicsConfiguration()
        .createCompatibleVolatileImage(x, y, Transparency.TRANSLUCENT);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.BLACK);
    graphics.setComposite(AlphaComposite.Clear);
    graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    graphics.dispose();
    return image;
  }
}
