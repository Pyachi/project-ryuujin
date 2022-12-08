package com.cs321.team1.game;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Resolutions;
import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Vec2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RenderingManager extends JPanel {

  private final JFrame window;
  private Vec2 windowedScreenSize = Resolutions._640x480.size;
  private int fullscreenMonitor = Arrays.stream(
          GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
      .collect(Collectors.toList())
      .indexOf(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
  private boolean fullscreen = false;
  private boolean debug = false;
  private Timer rendering;
  private Timer fpsTimer;
  private int tick = 0;
  private int measuredFPS = 0;
  private int fps = 0;

  RenderingManager() {
    window = new JFrame();
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Project 龍神");
    window.add(this);
    window.setLocationRelativeTo(null);
    setBackground(Color.BLACK);
    setDoubleBuffered(true);
    Controls.init(window);
    Font tempFont;
    try {
      tempFont = Font.createFont(Font.TRUETYPE_FONT,
          ResourceLoader.loadStream("resources/PressStart.ttf"));
    } catch (Exception e) {
      tempFont = new JLabel().getFont();
    }
    setFont(tempFont);
    Runtime.getRuntime().addShutdownHook(new Thread(this::saveOptions));
    startGameRendering();
  }

  public BufferedImage createImage() {
    return new BufferedImage(getScreenSize().x, getScreenSize().y, BufferedImage.TYPE_INT_ARGB);
  }

  public int getFPS() {
    return fps;
  }

  public void setFPS(int fps) {
    this.fps = fps;
    if (rendering != null) {
      rendering.cancel();
      rendering.purge();
      rendering = null;
    }
    startGameRendering();
  }

  public int getMonitor() {
    return fullscreenMonitor;
  }

  public void setMonitor(int monitor) {
    GraphicsDevice[] monitors = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices();
    if (monitor > monitors.length - 1) {
      return;
    }
    fullscreenMonitor = monitor;
  }

  public Vec2 getScreenSize() {
    Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices()[fullscreenMonitor].getDefaultConfiguration().getBounds();
    return fullscreen ? new Vec2(bounds.width, bounds.height) : windowedScreenSize;
  }

  public void setScreenSize(Vec2 res) {
    windowedScreenSize = res;
  }

  public boolean isFullscreen() {
    return fullscreen;
  }

  public void setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
  }

  public void toggleFullscreen() {
    setFullscreen(!fullscreen);
  }

  public void updateScreen() {
    window.dispose();
    window.setUndecorated(fullscreen);
    window.setVisible(true);
    GraphicsDevice[] monitors = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices();
    Rectangle bounds = monitors[fullscreenMonitor].getDefaultConfiguration().getBounds();
    if (fullscreen) {
      window.setLocation(bounds.x, bounds.y);
      setPreferredSize(new Dimension(bounds.width, bounds.height));
    } else {
      window.setLocation(bounds.x + (bounds.width - windowedScreenSize.x) / 2,
          bounds.y + (bounds.height - windowedScreenSize.y) / 2);
      setPreferredSize(windowedScreenSize.toDimension());
    }
    window.pack();
    Game.get().getSegments().forEach(GameSegment::onScreenSizeChange);
  }

  void loadOptions() {
    try (BufferedReader file = new BufferedReader(new FileReader("options.txt"))) {
      for (String str : file.lines().collect(Collectors.toList())) {
        try {
          if (str.startsWith("fullscreen: ")) {
            setFullscreen(Boolean.parseBoolean(str.split("fullscreen: ")[1]));
          }
          if (str.startsWith("screenWidth: ")) {
            setScreenSize(
                new Vec2(Integer.parseInt(str.split("screenWidth: ")[1]), getScreenSize().y));
          }
          if (str.startsWith("screenHeight: ")) {
            setScreenSize(
                new Vec2(getScreenSize().x, Integer.parseInt(str.split("screenHeight: ")[1])));
          }
          if (str.startsWith("monitor: ")) {
            setMonitor(Integer.parseInt(str.split("monitor: ")[1]));
          }
          if (str.startsWith("sound: ")) {
            Sounds.setVolume(Integer.parseInt(str.split("sound: ")[1]));
          }
          if (str.startsWith("music: ")) {
            Music.setVolume(Integer.parseInt(str.split("music: ")[1]));
          }
          if (str.startsWith("fps: ")) {
            setFPS(Integer.parseInt(str.split("fps: ")[1]));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception ignored) {
    }
  }

  void saveOptions() {
    try (FileWriter file = new FileWriter("options.txt")) {
      file.write("fullscreen: " + fullscreen + "\n");
      file.write("screenWidth: " + windowedScreenSize.x + "\n");
      file.write("screenHeight: " + windowedScreenSize.y + "\n");
      file.write("monitor: " + fullscreenMonitor + "\n");
      file.write("sound: " + Sounds.getVolume() + "\n");
      file.write("music: " + Music.getVolume() + "\n");
      file.write("fps: " + fps);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void toggleDebugMode() {
    debug = !debug;
    if (debug) {
      fpsTimer = new Timer();
      fpsTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          measuredFPS = tick;
          tick = 0;
        }
      }, 0L, 1000L);
    } else {
      fpsTimer.cancel();
      fpsTimer.purge();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    try {
      super.paintComponent(g);
      if (!Game.get().getSegments().isEmpty()) {
        g.drawImage(Game.get().getSegments().get(0).render(), 0, 0, null);
      }
      if (debug) {
        tick++;
        Font f = getFont().deriveFont(getScreenSize().y / 20F);
        FontMetrics metrics = g.getFontMetrics(f);
        g.setColor(new Color(0F, 0F, 0F, 0.5F));
        g.fillRect(getScreenSize().x - metrics.stringWidth("   ") * 2, 0,
            metrics.stringWidth("   ") * 3, metrics.getHeight() * 3);
        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(measuredFPS + "", (int) (getScreenSize().x - metrics.stringWidth("   ") * 1.5),
            metrics.getHeight() * 2);
      }
    } catch (Exception ignored) {
    }
  }

  private void startGameRendering() {
    rendering = new Timer();
    long period = fps == 0 ? 1000 / GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices()[fullscreenMonitor].getDisplayMode().getRefreshRate() : 1000 / fps;
    rendering.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        repaint();
      }
    }, 0L, period);
  }
}
