package com.cs321.team1.game;

import com.cs321.team1.map.Level;
import com.cs321.team1.menu.LoadingScreen;
import com.cs321.team1.util.Controls;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

  private static Game instance = null;
  private final List<GameSegment> segments = new ArrayList<>();
  private final Set<String> completedLevels = new HashSet<>();
  private final Renderer renderer = new Renderer();
  private final Settings settings = new Settings();
  private final Log log = new Log();

  private Game() {
    instance = this;
    Controls.init(renderer);
    renderer.updateScreen();
    renderer.start();
    startGameLogic();
  }

  public static Game get() {
    if (instance == null) instance = new Game();
    return instance;
  }

  public static Renderer getRenderer() {
    return get().renderer;
  }

  public static Settings getSettings() {
    return get().settings;
  }

  public static Log getLogger() {
    return get().log;
  }

  public GameSegment getHighestSegment() {
    try {
      return segments.get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public <T extends GameSegment> T getHighestSegmentOfType(Class<T> clazz) {
    return segments.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
  }

  public void popSegmentsTo(Class<? extends GameSegment> clazz) {
    while (!clazz.isInstance(segments.get(0))) {
      segments.remove(0).finish();
    }
    segments.get(0).restart();
  }

  public void pushSegment(GameSegment segment) {
    segment.start();
    segments.add(0, segment);
  }

  public void pushSegments(GameSegment... segments) {
    for (int i = 0; i < segments.length; i++) {
      var seg = segments[i];
      seg.start();
      this.segments.add(i, seg);
    }
  }

  public void removeSegment(GameSegment segment) {
    if (segments.remove(segment)) segment.finish();
  }

  public void completeLevel(String level) {
    completedLevels.add(level);
  }

  public boolean isLevelCompleted(String level) {
    return completedLevels.contains(level);
  }

  public void resetCompletedLevels() {
    completedLevels.clear();
  }

  public void saveGame() {
    try {
      var file = new FileWriter("ryuujin.sav");
      for (var level : completedLevels) {
        file.write("CMP|" + level + "\n");
      }
      for (var segment : segments) {
        if (!(segment instanceof Level)) return;
        file.write(segment.toString());
      }
      file.close();
    } catch (Exception e) {
      getLogger().error("An error has occurred while trying to save the game!", e);
    }
  }

  private void startGameLogic() {
    getLogger().info("Initializing game...");
    pushSegment(new LoadingScreen());
    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          update();
        } catch (Exception e) {
          Game.getLogger().error("An error has occurred during the game loop!", e);
        }
      }
    }, 0L, 20L);
    getLogger().info("Game initialized!");
  }

  private void update() {
    if (getHighestSegmentOfType(Controls.ControlChanger.class) == null) {
      if (Controls.FULLSCREEN.isPressed()) {
        settings.setFullscreen(!settings.isFullscreen());
        renderer.updateScreen();
      }
      if (Controls.DEBUG.isPressed()) settings.setDebug(!settings.isDebug());
    }
    segments.get(0).update();
  }

  public static void main(String[] args) {
    Game.get();
  }
}
