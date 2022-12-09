package com.cs321.team1.game;

import com.cs321.team1.util.ResourceUtil;
import com.cs321.team1.util.Texture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Renderer extends JFrame {

  private final JPanel panel = new JPanel();
  private final DebugOverlay debugOverlay = new DebugOverlay();
  private boolean disabled = false;

  Renderer() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setTitle("Project 龍神");
    setLocationRelativeTo(null);
    add(panel);
    setIconImage(new Texture("player/right", -1).buffer);
    try {
      setFont(
          Font.createFont(Font.TRUETYPE_FONT, ResourceUtil.loadStream("resources/PressStart.ttf")));
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  void start() {
    new Thread(() -> {
      while (true) {
        long start = System.nanoTime();
        if (!disabled) {
          render();
          debugOverlay.frames++;
        }
        long elapsed = System.nanoTime() - start;
        long wait = (Game.get().settings.getFramerate().getInterval() - elapsed) / 1000000;
        if (wait < 0) {
          wait = 0;
        }
        try {
          Thread.sleep(wait);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public void updateScreen() {
    disabled = true;
    dispose();
    setUndecorated(Game.get().settings.isFullscreen());
    var bounds = getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds();
    if (Game.get().settings.isFullscreen()) {
      setLocation(bounds.x, bounds.y);
      panel.setPreferredSize(new Dimension(bounds.width, bounds.height));
    } else {
      var screenSize = Game.get().settings.getScreenSize();
      setLocation(bounds.x + (bounds.width - screenSize.x()) / 2,
          bounds.y + (bounds.height - screenSize.y()) / 2);
      panel.setPreferredSize(new Dimension(screenSize.x(), screenSize.y()));
    }
    pack();
    setVisible(true);
    createBufferStrategy(2);
    disabled = false;
  }

  private void render() {
    var render = ResourceUtil.createImage();
    var graphics = render.createGraphics();
    if (Game.get().getHighestSegment() != null) {
      Game.get().getHighestSegment().render(graphics);
    }
    if (Game.get().settings.isDebug()) {
      debugOverlay.render(graphics);
    }
    if (!disabled) {
      var buffer = (Graphics2D) getBufferStrategy().getDrawGraphics();
      buffer.setColor(Color.BLACK);
      buffer.fillRect(0, 0, getWidth(), getHeight());
      buffer.translate(getInsets().right, getInsets().top);
      buffer.drawImage(render, 0, 0, getWidth(), getHeight(), null);
      render.flush();
      if (!disabled) { //Because race conditions yay
        getBufferStrategy().show();
      }
    }
  }
}
