package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.crates.Crate;

public class Conveyor extends GameObject {

  private final int x;
  private final int y;

  private Conveyor(Vec2 loc, int x, int y) {
    this.x = x;
    this.y = y;
    setLocation(loc);
    if (y == -1) {
      setTexture("map/conveyor_up_animated");
    } else if (y == 1) {
      setTexture("map/conveyor_down_animated");
    } else if (x == -1) {
      setTexture("map/conveyor_left_animated");
    } else if (x == 1) {
      setTexture("map/conveyor_right_animated");
    }
  }

  public static Conveyor DOWN(Vec2 loc) {
    return new Conveyor(loc, 0, 1);
  }

  public static Conveyor LEFT(Vec2 loc) {
    return new Conveyor(loc, -1, 0);
  }

  public static Conveyor RIGHT(Vec2 loc) {
    return new Conveyor(loc, 1, 0);
  }

  public static Conveyor UP(Vec2 loc) {
    return new Conveyor(loc, 0, -1);
  }

  @Tick(priority = 3)
  public void move() {
    getCollisions(Player.class).forEach(it -> {
      if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y)) {
        it.move(x, y);
      }
    });
    getCollisions(Crate.class).forEach(it -> {
      if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y) && !it.isGrabbed()) {
        it.move(x, y);
      }
    });
  }

  @Override
  public String toString() {
    String type;
    switch (x) {
      case -1:
        type = "L";
        break;
      case 1:
        type = "R";
        break;
      default:
        switch (y) {
          case -1:
            type = "U";
            break;
          case 1:
            type = "D";
            break;
          default:
            type = "X";
            break;
        }
    }
    return "CVR|" + getLocation().toString() + "|" + type;
  }

  private void setTexture(String path) {
    setTexture(new Texture(path, 0));
  }
}
