package com.cs321.team1.game;

import com.cs321.team1.util.Vec2;
import com.cs321.team1.util.audio.Music;
import com.cs321.team1.util.audio.Sounds;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Settings {

  Settings() {
    loadOptions();
    Runtime.getRuntime().addShutdownHook(new Thread(this::saveOptions));
  }

  private boolean debug;
  private boolean fullscreen;
  private Resolution resolution = Resolution._640x480;
  private Framerate framerate = Framerate.VSYNC;

  public Vec2 getScreenSize() {
    return resolution.size;
  }

  public Resolution getResolution() {
    return resolution;
  }

  public void setResolution(Resolution screenSize) {
    this.resolution = screenSize;
  }

  public Framerate getFramerate() {
    return framerate;
  }

  public void setFramerate(Framerate framerate) {
    this.framerate = framerate;
  }

  public boolean isFullscreen() {
    return fullscreen;
  }

  public void setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  void loadOptions() {
    try (var file = new BufferedReader(new FileReader("options.txt"))) {
      for (var str : file.lines().toList()) {
        try {
          if (str.startsWith("fullscreen: ")) {
            setFullscreen(Boolean.parseBoolean(str.split("fullscreen: ")[1]));
          }
          if (str.startsWith("resolution: ")) {
            setResolution(Resolution.values()[Integer.parseInt(str.split("resolution: ")[1])]);
          }
          if (str.startsWith("framerate: ")) {
            setFramerate(Framerate.values()[Integer.parseInt(str.split("framerate: ")[1])]);
          }
          if (str.startsWith("music: ")) {
            Music.setVolume(Integer.parseInt(str.split("music: ")[1]));
          }
          if (str.startsWith("sound: ")) {
            Sounds.setVolume(Integer.parseInt(str.split("sound: ")[1]));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception ignored) {
    }
  }

  void saveOptions() {
    try (var file = new FileWriter("options.txt")) {
      file.write("fullscreen: " + fullscreen + "\n");
      file.write("resolution: " + resolution.ordinal() + "\n");
      file.write("framerate: " + framerate.ordinal() + "\n");
      file.write("music: " + Music.getVolume() + "\n");
      file.write("sound: " + Sounds.getVolume() + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
