package com.cs321.team1.map;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
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
//    VolatileImage image = ResourceUtil.createImage();
//    Graphics g = image.getGraphics();
//    VolatileImage lvlImage = tick <= 10 ? from.render() : to.render();
//    g.setColor(Color.BLACK);
//    if (lvlImage != null) {
//      g.drawImage(lvlImage, (image.getWidth(null) - lvlImage.getWidth()) / 2,
//          (image.getHeight() - lvlImage.getHeight()) / 2, lvlImage.getWidth(), lvlImage.getHeight(),
//          null);
//    }
//    if (tick <= 10) {
//      g.fillRect((int) (image.getWidth() - (image.getWidth() * (tick / 10.0))), 0,
//          (int) (image.getWidth() * (tick / 10.0)), image.getHeight());
//    } else {
//      g.fillRect(0, 0, (int) (image.getWidth() * ((20.0 - tick) / 10.0)), image.getHeight());
//    }
//    return image;
  }

  @Override
  public void update() {
    tick++;
    if (tick <= 10) {
      from.update();
    } else {
      to.update();
    }
    if (tick > 20) {
      Game.get().removeSegment(this);
    }
  }
}
