package com.cs321.team1.obj.world;

import com.cs321.team1.obj.GameObject;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class Navigator extends GameObject {

  private boolean moving = false;
  private int moveTick = 0;
  private int moveX = 0;
  private int moveY = 0;

  public Navigator(Vec2 loc) {
    setTexture(new Texture("player/nav", 5));
    setSize(new Vec2(16, 16));
    setLocation(loc);
    registerTick(1, this::checkMovement);
  }

  private void checkMovement() {
    if (moving) {
      moveTick--;
      move(moveX, moveY);
      if (moveTick == 0) {
        moving = false;
      }
    } else {
      if (Controls.UP.isPressed() && canMove(0, -1)) {
        startMoving(0, -1);
      } else if (Controls.DOWN.isPressed() && canMove(0, 1)) {
        startMoving(0, 1);
      } else if (Controls.LEFT.isPressed() && canMove(-1, 0)) {
        startMoving(-1, 0);
      } else if (Controls.RIGHT.isPressed() && canMove(1, 0)) {
        startMoving(1, 0);
      }
    }
  }

  @Override
  public String toString() {
    return "NAV|" + getLocation().toString();
  }

  private boolean canMove(int x, int y) {
    move(x * 16, y * 16);
    boolean collision = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
    move(-x * 16, -y * 16);
    return collision;
  }

  private void startMoving(int x, int y) {
    moveX = x * 2;
    moveY = y * 2;
    moving = true;
    moveTick = 8;
  }
}
