package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;


public class Particle extends GameObject {

  public final int lifetime;


  public Particle(Vec2 loc, Texture texture) {
    setLocation(loc.add(new Vec2(-((texture.size.x() - 16) / 2), -((texture.size.y() - 16) / 2))));
    setTexture(texture);
    lifetime = 5 * texture.frames;
  }


  @Tick(priority = 4)
  public void deathClock() {
    if (getTick() >= lifetime) {
      kill();
    }
  }

  @Override
  public String toString() {
    return "NULL|" + getLocation().toString();
  }
}
