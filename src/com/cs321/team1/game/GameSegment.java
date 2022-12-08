package com.cs321.team1.game;

import java.awt.image.BufferedImage;

public interface GameSegment {

  default void finish() {
  }

  default void onScreenSizeChange() {
  }

  default void refresh() {
  }

  BufferedImage render();

  default void start() {
  }

  default void update() {
  }
}
