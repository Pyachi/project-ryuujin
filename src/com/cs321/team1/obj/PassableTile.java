package com.cs321.team1.obj;

import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class PassableTile extends GameObject {

  public PassableTile(Vec2 loc, Vec2 size, Texture tex) {
    if (tex != null) setTexture(tex);
    setLocation(loc);
    setSize(size);
  }

  public PassableTile(Vec2 loc, Texture tex) {
    this(loc, tex.size, tex);
  }

  public PassableTile(Vec2 loc, Vec2 size) {
    this(loc, size, null);
  }

  @Override
  public String toString() {
    return "FLR|" + getLocation().toString() + "|" + getSize().toString() + "|"
        + getTexture().toString();
  }
}
