package com.cs321.team1.map;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import java.awt.Color;
import java.awt.Graphics2D;

public class LevelTransition implements GameSegment {

  private final GameSegment from;
  private final GameSegment to;
  private int tick = 0;

  public LevelTransition(GameSegment from, GameSegment to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void render(Graphics2D buffer) {
    from.render(buffer);
    if (tick <= 20) {
      from.render(buffer);
    } else {
      to.render(buffer);
    }
    buffer.setColor(Color.BLACK);
    if (tick <= 20) {
      buffer.fillRect(
          (int) (Game.get().settings.getScreenSize().x() - (Game.get().settings.getScreenSize().x()
              * (tick / 20.0))), 0, (int) (Game.get().settings.getScreenSize().x() * (tick / 20.0)),
          Game.get().settings.getScreenSize().y());
    } else {
      buffer.fillRect(0, 0,
          (int) (Game.get().settings.getScreenSize().x() * ((40.0 - tick) / 20.0)),
          Game.get().settings.getScreenSize().y());
    }
  }

  @Override
  public void update() {
    tick++;
    if (tick > 40) {
      Game.get().removeSegment(this);
    }
  }
}
