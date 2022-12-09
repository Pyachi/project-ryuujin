package com.cs321.team1.game;

import java.awt.Graphics2D;

public interface GameSegment {

  default void start() {
  }

  default void restart() {
  }

  default void finish() {
  }

  default void update() {
  }

  default void render(Graphics2D buffer) {
  }
}
