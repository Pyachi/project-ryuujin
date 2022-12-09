package com.cs321.team1.obj;

import com.cs321.team1.obj.crates.Crate;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class Player extends GameObject {

  private boolean moving = false;
  private int moveTick = 0;
  private int moveX = 0;
  private int moveY = 0;
  private Controls buffered;

  public Player(Vec2 loc) {
    setTexture("player/right_idle_animated");
    setSize(new Vec2(1, 1).toTile());
    setLocation(loc);
    registerTick(1, this::checkMovement);
  }

  public boolean canMove(int x, int y) {
    move(x, y);
    boolean collision = collidesWith(Player.class) || collidesWith(UnpassableTile.class)
        || collidesWith(Crate.class) && getCollisions(Crate.class).stream()
        .anyMatch(crate -> !crate.canMove(x, y));
    move(-x, -y);
    return !collision;
  }

  @Override
  public String toString() {
    return "PLR|" + getLocation().toString();
  }

  private void checkMovement() {
    if (moveTick < 5) {
      if (Controls.UP.isPressed()) {
        buffered = Controls.UP;
      } else if (Controls.DOWN.isPressed()) {
        buffered = Controls.DOWN;
      } else if (Controls.LEFT.isPressed()) {
        buffered = Controls.LEFT;
      } else if (Controls.RIGHT.isPressed()) {
        buffered = Controls.RIGHT;
      }
    }
    if (moving) {
      moveTick--;
      super.move(moveX, moveY);
      if (moveTick == 0) {
        moving = false;
        switch (getTexture().path.replace("player/", "").split("_")[0]) {
          case "up" -> setTexture("player/up_idle_animated");
          case "down" -> setTexture("player/down_idle_animated");
          case "left" -> setTexture("player/left_idle_animated");
          default -> setTexture("player/right_idle_animated");
        }
      }
    } else {
      if (buffered == Controls.UP && canMove(0, -1)) {
        startMoving(0, -1);
        setTexture("player/up_walk_animated");
      } else if (buffered == Controls.DOWN && canMove(0, 1)) {
        startMoving(0, 1);
        setTexture("player/down_walk_animated");
      } else if (buffered == Controls.LEFT  && canMove(-1, 0)) {
        startMoving(-1, 0);
        setTexture("player/left_walk_animated");
      } else if (buffered == Controls.RIGHT && canMove(1, 0)) {
        startMoving(1, 0);
        setTexture("player/right_walk_animated");
      }
    }
  }

  public void startMoving(int x, int y) {
    move(x,y);
    getCollisions(Crate.class).forEach(it -> it.startMoving(x,y));
    move(-x,-y);
    buffered = null;
    moveX = x;
    moveY = y;
    moving = true;
    moveTick = 16;
  }

  private void setTexture(String path) {
    setTexture(new Texture(path, 2));
  }
}
