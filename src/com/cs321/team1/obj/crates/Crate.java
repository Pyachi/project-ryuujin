package com.cs321.team1.obj.crates;

import com.cs321.team1.game.Game;
import com.cs321.team1.obj.GameObject;
import com.cs321.team1.obj.Player;
import com.cs321.team1.obj.UnpassableTile;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;
import com.cs321.team1.util.audio.Sounds;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Crate extends GameObject {

  private boolean moving = false;
  private int moveTick = 0;
  private int moveX = 0;
  private int moveY = 0;
  private final int value;

  public Crate(Vec2 loc, String texturePath, int value) {
    this.value = value;
    setLocation(loc);
    setSize(new Vec2(1, 1).toTile());
    setTexture(new Texture(texturePath, 1));
    registerTick(2, this::tick);
  }

  public void startMoving(int x, int y) {
    moveX = x;
    moveY = y;
    moving = true;
    moveTick = 16;
  }

  public boolean isMovable() {
    return true;
  }

  public abstract boolean canBeAppliedTo(Crate other);

  public boolean canMove(int x, int y) {
    if (!isMovable()) return false;
    move(x, y);
    boolean collision =
        collidesWith(Player.class) || collidesWith(UnpassableTile.class) || getCollisions(
            Crate.class).stream().anyMatch(it -> !canBeAppliedTo(it) && !it.canBeAppliedTo(this));
    move(-x, -y);
    return !collision;
  }

  private void tick() {
    if (moving) {
      moveTick--;
      move(moveX, moveY);
      if (moveTick == 0) moving = false;
    } else {
      getCollisions(Crate.class).stream().filter(crate -> crate.getLocation().equals(getLocation()))
          .filter(this::canBeAppliedTo).findAny().ifPresent(this::generateNew);
    }
  }

  public abstract Crate getMergedCrate(Vec2 loc, Crate other);

  public String getString() {
    return Integer.toString(getValue());
  }

  public int getValue() {
    return value;
  }

  @Override
  public void paint(Graphics2D buffer) {
    if (isDead()) return;
    super.paint(buffer);
    buffer.setFont(Game.getRenderer().getFont().deriveFont(
        (float) 16 * getLevel().getScale() * 0.4F / buffer.getFontMetrics(
            Game.getRenderer().getFont()).stringWidth(getString())));
    buffer.setColor(Color.WHITE);
    buffer.drawString(getString(), getLocation().x() * getLevel().getScale()
            - buffer.getFontMetrics().stringWidth(getString()) / 2 - 8 * getLevel().getScale(),
        getLocation().y() * getLevel().getScale() + buffer.getFontMetrics().getHeight() / 2
            - 7 * getLevel().getScale());
  }

  private void generateNew(Crate other) {
    if (isDead()) return;
    Crate newCrate = getMergedCrate(getLocation(), other);
    if (newCrate != null) getLevel().addObject(newCrate);
    Sounds.MERGE.play();
    other.kill();
    kill();
  }
}
