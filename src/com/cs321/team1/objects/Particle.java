package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;

/**
 * A static object that destroys itself after one animation cycle.
 */
public class Particle extends GameObject {
    public final int lifetime;

    public Particle(Location loc, Texture texture) {
        setLocation(loc.add(-((texture.size.w() - 16) / 2), -((texture.size.h() - 16) / 2)));
        setTexture(texture);
        lifetime = 5 * texture.frames;
    }

    @Tick(priority = 4)
    public void deathClock() {
        if (tick >= lifetime) kill();
    }

    @Override
    public String toString() {
        return "NULL|" + getLocation().toString();
    }
}
