package com.cs321.team1.obj.world;

import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.obj.GameObject;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;
import java.awt.Graphics2D;

public class LevelObject extends GameObject {

  private final String lvl;

  public LevelObject(Vec2 loc, String lvl) {
    this.lvl = lvl;
    setTexture(new Texture("map/level", 2));
    setSize(new Vec2(16, 16));
    setLocation(loc);
    registerTick(1, this::onTick);
  }

  private void onTick() {
    if (Game.get().isLevelCompleted(lvl)) {
      setTexture(new Texture("map/completedLevel", 2));
    }
    if (getCollisions(Navigator.class).stream()
        .anyMatch(it -> it.getLocation().equals(getLocation())) && Controls.SELECT.isPressed()) {
      Level level = Level.load(lvl);
      if (level != null) {
        Game.get().pushSegments(new LevelTransition(getLevel(), level), level);
      }
    }
  }

  @Override
  public void paint(Graphics2D buffer) {
    if (isDead()) {
      return;
    }
    super.paint(buffer);
    buffer.setFont(Game.getRenderer().getFont().deriveFont(
        (float) 16 * getLevel().getScale() * 0.5F / buffer.getFontMetrics(
            Game.getRenderer().getFont()).stringWidth(lvl + "")));
    buffer.drawString(lvl + "", getLocation().x() * getLevel().getScale()
            - buffer.getFontMetrics().stringWidth(lvl + "") / 2 - 8 * getLevel().getScale(),
        getLocation().y() * getLevel().getScale() + buffer.getFontMetrics().getHeight() / 2
            - 8 * getLevel().getScale());
  }

  @Override
  public String toString() {
    return "LVL|" + getLocation().toString() + "|" + lvl;
  }
}
