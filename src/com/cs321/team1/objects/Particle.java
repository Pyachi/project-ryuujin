package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;

/**
 * A static object that destroys itself after one animation cycle.
 */
public class Particle extends GameObject {
    public final int lifetime;
    
    public Particle(Location loc, Texture texture) {
        super(1, 1);
        setLocation(loc.add(-((texture.width - 16) / 2), -((texture.height - 16) / 2)));
        setTexture(texture);
        lifetime = 5 * texture.frames;
    }
    
    @Tick(priority = 4)
    public void deathClock() {
        if (tick >= lifetime) kill();
    }
}
