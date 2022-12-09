package com.cs321.team1.obj;

import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class Particle extends GameObject {

  public final int lifetime;

  public Particle(Vec2 loc, Texture texture) {
    setLocation(loc.add(-((texture.size.x() - 16) / 2), -((texture.size.y() - 16) / 2)));
    setTexture(texture);
    lifetime = 5 * texture.frames;
    registerTick(1, this::deathClock);
  }

  private void deathClock() {
    if (getAge() >= lifetime) {
      kill();
    }
  }

  @Override
  public String toString() {
    return "NULL|" + getLocation().toString();
  }
}
