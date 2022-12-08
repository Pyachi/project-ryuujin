package com.cs321.team1.map;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class LevelTransition implements GameSegment {

  private final GameSegment from;
  private final GameSegment to;
  private int tick = 0;

  public LevelTransition(GameSegment from, GameSegment to) {
    this.from = from;
    this.to = to;
    from.render();
    to.render();
  }

  @Override
  public BufferedImage render() {
    var image = Game.get().getRenderingManager().createImage();
    var g = image.createGraphics();
    var lvlImage = tick <= 20 ? from.render() : to.render();
    g.setColor(Color.BLACK);
    if (lvlImage != null) {
      g.drawImage(lvlImage, (image.getWidth() - lvlImage.getWidth()) / 2,
          (image.getHeight() - lvlImage.getHeight()) / 2, lvlImage.getWidth(), lvlImage.getHeight(),
          null);
    }
    if (tick <= 20) {
      g.fillRect((int) (image.getWidth() - (image.getWidth() * (tick / 20.0))), 0,
          (int) (image.getWidth() * (tick / 20.0)), image.getHeight());
    } else {
      g.fillRect(0, 0, (int) (image.getWidth() * ((40.0 - tick) / 20.0)), image.getHeight());
    }
    return image;
  }

  @Override
  public void update() {
    tick++;
    if (tick <= 20) {
      from.update();
    } else {
      to.update();
    }
    if (tick > 40) {
      Game.get().removeSegment(this);
    }
  }
}
