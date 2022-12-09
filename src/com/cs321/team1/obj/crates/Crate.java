package com.cs321.team1.obj.crates;

import com.cs321.team1.game.Game;
import com.cs321.team1.obj.GameObject;
import com.cs321.team1.obj.Particle;
import com.cs321.team1.obj.Player;
import com.cs321.team1.obj.UnpassableTile;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;
import com.cs321.team1.util.audio.Sounds;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Crate extends GameObject {

  private final int value;

  public Crate(Vec2 loc, String texturePath, int value) {
    this.value = value;
    setLocation(loc);
    setSize(new Vec2(1, 1).toTile());
    setTexture(new Texture(texturePath, 1));
    registerTick(2, this::checkMerge);
  }

  public abstract boolean canBeAppliedTo(Crate other);

  public boolean canGrab() {
    return true;
  }

  public boolean canMove(int x, int y) {
    move(x, y);
    boolean collision =
        getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this)
            || collidesWith(UnpassableTile.class) || getCollisions(Crate.class).stream()
            .anyMatch(it -> !canBeAppliedTo(it) && !it.canBeAppliedTo(this));
    move(-x, -y);
    return !collision;
  }

  private void checkMerge() {
    getCollisions(Crate.class).stream().filter(this::canBeAppliedTo).findAny()
        .ifPresent(this::generateNew);
  }

  public Player getGrabber() {
    return getLevel().getObjects().stream().filter(Player.class::isInstance).map(Player.class::cast)
        .filter(it -> it.getGrabbedCrate() == this).findFirst().orElse(null);
  }

  public abstract Crate getMergedCrate(Vec2 loc, Crate other);

  public String getString() {
    return Integer.toString(getValue());
  }

  public int getValue() {
    return value;
  }

  public boolean isGrabbed() {
    return getGrabber() != null;
  }

  @Override
  public void paint(Graphics2D buffer) {
    if (isDead()) {
      return;
    }
    super.paint(buffer);
    buffer.setFont(Game.get().renderer.getFont().deriveFont(
        (float) 16 * getLevel().getScale() * 0.4F / buffer.getFontMetrics(Game.get().renderer.getFont())
            .stringWidth(getString())));
    buffer.setColor(Color.WHITE);
    buffer.drawString(getString(),
        getLocation().x() * getLevel().getScale() - buffer.getFontMetrics().stringWidth(getString()) / 2
            - 8 * getLevel().getScale(),
        getLocation().y() * getLevel().getScale() + buffer.getFontMetrics().getHeight() / 2
            - 7 * getLevel().getScale());
  }

  private void generateNew(Crate other) {
    if (isDead()) {
      return;
    }
    Crate replacedCrate = (!other.canGrab() || canBeAppliedTo(other)) ? other : this;
    Crate newCrate = getMergedCrate(replacedCrate.getLocation(), other);
    if (newCrate != null) {
      getLevel().addObject(newCrate);
      getLevel().addObject(new Particle((replacedCrate == this ? other : this).getLocation(),
          new Texture("crates/explosion_animated", 4)));
    }
    Sounds.MERGE.play();
    if (replacedCrate.isGrabbed()) {
      replacedCrate.getGrabber().setGrabbedCrate(newCrate);
    }
    other.kill();
    kill();
  }
}
