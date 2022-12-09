package com.cs321.team1.obj;

import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class UnpassableTile extends GameObject {

  public UnpassableTile(Vec2 loc, Vec2 size, Texture tex) {
    setTexture(tex);
    setLocation(loc);
    setSize(size);
  }

  public UnpassableTile(Vec2 loc, Texture tex) {
    setTexture(tex);
    setLocation(loc);
    setSize(tex.size);
  }

  public UnpassableTile(Vec2 loc, Vec2 size) {
    setLocation(loc);
    setSize(size);
  }

  @Override
  public String toString() {
    return "WAL|" + getLocation().toString() + "|" + getSize().toString() + "|"
        + getTexture().toString();
  }
}
