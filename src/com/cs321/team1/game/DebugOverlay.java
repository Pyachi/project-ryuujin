package com.cs321.team1.game;

import com.cs321.team1.util.ResourceUtil;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.Timer;
import java.util.TimerTask;

public class DebugOverlay {

  int frames;
  private int fps;

  DebugOverlay() {
    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        fps = frames;
        frames = 0;
      }
    }, 0, 1000);
  }

  void render(Graphics2D graphics) {
    var font = Game.get().renderer.getFont()
        .deriveFont(Game.get().settings.getScreenSize().y() / 40F);
    var metrics = graphics.getFontMetrics(font);

    graphics.setColor(Color.BLACK);
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
    graphics.fillRect(0, 0, Game.get().settings.getScreenSize().x() / 2,
        Game.get().settings.getScreenSize().y());

    graphics.setColor(Color.WHITE);
    graphics.setComposite(AlphaComposite.SrcOver);
    graphics.setFont(font);
    graphics.drawString("FPS: " + fps, metrics.getHeight(), metrics.getHeight() * 2);
    graphics.drawString("FD: " + Game.get().settings.getFramerate().getInterval(), metrics.getHeight(),
        metrics.getHeight() * 4);
    graphics.drawString("ST: " + Game.get().getHighestSegment().getClass().getSimpleName(),
        metrics.getHeight(), metrics.getHeight() * 6);
  }
}
