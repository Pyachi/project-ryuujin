package com.cs321.team1.game;

import com.cs321.team1.map.Level;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
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
    var font = Game.getRenderer().getFont()
        .deriveFont(Game.getSettings().getScreenSize().y() / 40F);
    var metrics = graphics.getFontMetrics(font);

    graphics.setColor(Color.BLACK);
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
    graphics.fillRect(0, 0, Game.getSettings().getScreenSize().x() / 3,
        Game.getSettings().getScreenSize().y() / 3);

    graphics.setColor(Color.WHITE);
    graphics.setComposite(AlphaComposite.SrcOver);
    graphics.setFont(font);
    graphics.drawString("FPS: " + fps, metrics.getHeight(), metrics.getHeight() * 2);
    graphics.drawString("FD: " + Game.getSettings().getFramerate().getInterval(),
        metrics.getHeight(), metrics.getHeight() * 4);
    graphics.drawString("ST: " + Game.get().getHighestSegment().getClass().getSimpleName(),
        metrics.getHeight(), metrics.getHeight() * 6);
    Level level = Game.get().getHighestSegmentOfType(Level.class);
    graphics.drawString("LO: " + (level == null ? 0 : level.getObjects().size()),
        metrics.getHeight(), metrics.getHeight() * 8);
  }
}
