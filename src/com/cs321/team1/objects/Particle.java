package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

/**
 * GameObject that plays an animation once and then kills itself
 */
public class Particle extends GameObject {
    /**
     * Maximum lifetime of object
     */
    public final int lifetime;
    
    /**
     * Creates a particle at the given location with the given texture
     *
     * @param loc     The location of the particle
     * @param texture the texture of the particle
     */
    public Particle(Vec2 loc, Texture texture) {
        setLocation(loc.add(new Vec2(-((texture.size.x() - 16) / 2), -((texture.size.y() - 16) / 2))));
        setTexture(texture);
        lifetime = 5 * texture.frames;
    }
    
    /**
     * Kills the particle if alive longer than should be
     */
    @Tick(priority = 4)
    public void deathClock() {
        if (getTick() >= lifetime) kill();
    }
    
    @Override
    public String toString() {
        return "NULL|" + getLocation().toString();
    }
}
