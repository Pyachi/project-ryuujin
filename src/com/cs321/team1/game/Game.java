package com.cs321.team1.game;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.LoadingScreen;
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
  private final RenderingManager renderingManager = new RenderingManager();

  private Game() {
    instance = this;
    renderingManager.loadOptions();
    renderingManager.updateScreen();
    startGameLogic();
  }

  public static Game get() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }

  public static void main(String[] args) {
    Game.get();
  }

  public void completeLevel(String lvl) {
    completedLevels.add(lvl);
  }

  public GameSegment getHighestSegment() {
    return segments.get(0);
  }

  public <T extends GameSegment> T getHighestSegmentOfType(Class<T> clazz) {
    return segments.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
  }

  public RenderingManager getRenderingManager() {
    return renderingManager;
  }

  public GameSegment getSegmentAtIndex(int index) {
    return segments.get(index);
  }

  public boolean isLevelCompleted(String lvl) {
    return completedLevels.contains(lvl);
  }

  public void popSegmentsTo(Class<? extends GameSegment> clazz) {
    while (!clazz.isInstance(segments.get(0))) {
      segments.remove(0).finish();
    }
    segments.get(0).refresh();
  }

  public void pushSegment(GameSegment seg) {
    seg.start();
    segments.add(0, seg);
  }

  public void pushSegments(GameSegment... segs) {
    for (int i = 0; i < segs.length; i++) {
      GameSegment seg = segs[i];
      seg.start();
      segments.add(i, seg);
    }
  }

  public void removeSegment(GameSegment seg) {
    if (segments.remove(seg)) {
      seg.finish();
    }
  }

  public void resetCompletedLevels() {
    completedLevels.clear();
  }

  public void saveGame() {
    try {
      FileWriter file = new FileWriter("ryuujin.sav");
      completedLevels.forEach(lvl -> {
        try {
          file.write("CMP|" + lvl + "\n");
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      segments.forEach(seg -> {
        if (!(seg instanceof Level)) {
          return;
        }
        try {
          file.write(seg.toString());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  List<GameSegment> getSegments() {
    return new ArrayList<>(segments);
  }

  private void startGameLogic() {
    pushSegment(new LoadingScreen());
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        update();
      }
    }, 0L, 20L);
  }

  private void update() {
    try {
      if (getHighestSegmentOfType(Controls.ControlChanger.class) == null) {
        if (Controls.FULLSCREEN.isPressed()) {
          renderingManager.toggleFullscreen();
          renderingManager.updateScreen();
        }
        if (Controls.DEBUG.isPressed()) {
          renderingManager.toggleDebugMode();
        }
      }
      segments.get(0).update();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
