package com.cs321.team1.game;

import java.util.function.Supplier;

public enum Framerate {
  UNLIMITED(() -> 1000000000, "Unlimited"),
  VSYNC(() -> Game.getRenderer().getGraphicsConfiguration().getDevice().getDisplayMode()
      .getRefreshRate(), "VSYNC"),
  _30(() -> 30, "30"),
  _60(() -> 60, "60"),
  _75(() -> 75, "75"),
  _90(() -> 90, "90"),
  _120(() -> 120, "120"),
  _144(() -> 144, "144"),
  _240(() -> 240, "240");
  private final Supplier<Integer> fps;
  private final String name;

  Framerate(Supplier<Integer> fps, String name) {
    this.fps = fps;
    this.name = name;
  }

  public int getInterval() {
    return 1000000000 / fps.get();
  }

  public String getName() {
    return name;
  }
}
