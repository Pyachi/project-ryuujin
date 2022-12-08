package com.cs321.team1.objects.world;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.Tick;

public class NavPath extends GameObject {

  private boolean measured;

  public NavPath(Vec2 loc) {
    setSize(new Vec2(16, 16));
    setLocation(loc);
  }

  @Tick
  public void checkDirections() {
    if (measured) {
      return;
    }
    measured = true;
    move(0, -16);
    boolean up = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
    move(0, 32);
    boolean down = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
    move(0, -16);
    move(-16, 0);
    boolean left = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
    move(32, 0);
    boolean right = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
    move(-16, 0);
    int value = (up ? 1 : 0) + (down ? 2 : 0) + (left ? 4 : 0) + (right ? 8 : 0);
    switch (value) {
      default:
        setTexture(new Texture("map/path", 2));
        break;
      case 1:
        setTexture(new Texture("map/path_u", 2));
        break;
      case 2:
        setTexture(new Texture("map/path_d", 2));
        break;
      case 3:
        setTexture(new Texture("map/path_ud", 2));
        break;
      case 4:
        setTexture(new Texture("map/path_l", 2));
        break;
      case 5:
        setTexture(new Texture("map/path_ul", 2));
        break;
      case 6:
        setTexture(new Texture("map/path_dl", 2));
        break;
      case 7:
        setTexture(new Texture("map/path_udl", 2));
        break;
      case 8:
        setTexture(new Texture("map/path_r", 2));
        break;
      case 9:
        setTexture(new Texture("map/path_ur", 2));
        break;
      case 10:
        setTexture(new Texture("map/path_dr", 2));
        break;
      case 11:
        setTexture(new Texture("map/path_udr", 2));
        break;
      case 12:
        setTexture(new Texture("map/path_lr", 2));
        break;
      case 13:
        setTexture(new Texture("map/path_ulr", 2));
        break;
      case 14:
        setTexture(new Texture("map/path_dlr", 2));
        break;
      case 15:
        setTexture(new Texture("map/path_udlr", 2));
        break;
    }
  }

  @Override
  public String toString() {
    return "PTH|" + getLocation().toString();
  }
}
