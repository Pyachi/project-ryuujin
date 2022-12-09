package com.cs321.team1.obj;

import com.cs321.team1.obj.crates.Crate;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

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
    registerTick(3, this::move);
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

  private void move() {
//    getCollisions(Player.class).forEach(it -> {
//      if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y)) {
//        it.move(x, y);
//      }
//    });
//    getCollisions(Crate.class).forEach(it -> {
//      if (it.getCollisions(Conveyor.class).get(0) == this && it.canMove(x, y) && !it.isGrabbed()) {
//        it.move(x, y);
//      }
//    });
  }

  @Override
  public String toString() {
    return "CVR|" + getLocation().toString() + "|" + switch (x) {
      case -1 -> "L";
      case 1 -> "R";
      default -> switch (y) {
        case -1 -> "U";
        case 1 -> "D";
        default -> "X";
      };
    };
  }

  private void setTexture(String path) {
    setTexture(new Texture(path, 0));
  }
}
