package com.cs321.team1.obj;

import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class PassableTile extends GameObject {

  public PassableTile(Vec2 loc, Vec2 size, Texture tex) {
    setTexture(tex);
    setLocation(loc);
    setSize(size);
  }

  public PassableTile(Vec2 loc, Texture tex) {
    setTexture(tex);
    setLocation(loc);
    setSize(tex.size);
  }

  public PassableTile(Vec2 loc, Vec2 size) {
    setLocation(loc);
    setSize(size);
  }

  @Override
  public String toString() {
    return "FLR|" + getLocation().toString() + "|" + getSize().toString() + "|"
        + getTexture().toString();
  }
}
