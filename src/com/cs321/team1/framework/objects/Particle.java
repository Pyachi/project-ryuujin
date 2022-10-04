package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.textures.Texture;

public class Particle extends GameObject {
    public final int lifetime;
    
    public Particle(Location loc, Texture texture) {
        setLocation(loc.move(-((texture.getWidth() - 16) / 2), -((texture.getHeight() - 16) / 2)));
        setTexture(texture);
        lifetime = 5 * texture.getTexture().frames;
    }
    
    @Tick(priority = 4)
    public void deathClock() {
        if (tick >= lifetime) kill();
    }
}
