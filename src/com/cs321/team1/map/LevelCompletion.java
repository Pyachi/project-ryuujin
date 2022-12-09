package com.cs321.team1.map;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.ResourceUtil;
import com.cs321.team1.util.Texture;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class LevelCompletion implements GameSegment {

  private final Level level;
  private final Texture texture = new Texture("map/completion", 10);
  private int tick = 0;

  public LevelCompletion(Level level) {
    this.level = level;
  }

  @Override
  public void render(Graphics2D buffer) {
    level.render(buffer);
//    VolatileImage completionImage = ResourceUtil.createImage((int) (lvlImage.getWidth() * (tick / 50.0)),
//        (int) (lvlImage.getHeight() * (tick / 50.0)));
//    texture.fillImage(completionImage, tick);
//    lvlImage.getGraphics()
//        .drawImage(completionImage, (lvlImage.getWidth() - completionImage.getWidth()) / 2,
//            (lvlImage.getHeight() - completionImage.getHeight()) / 2, completionImage.getWidth(),
//            completionImage.getHeight(), null);
  }

  @Override
  public void update() {
    if (tick < 50) {
      tick++;
    }
    if (Controls.SELECT.isPressed() && tick == 50) {
//      Game.get().pushSegment(new LevelTransition(this, Game.get().getSegmentAtIndex(2)));
      Game.get().removeSegment(Game.get().getHighestSegmentOfType(Level.class));
      Game.get().removeSegment(this);
    }
  }
}
