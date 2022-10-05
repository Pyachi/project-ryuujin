package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;

/**
 * A static tile that runs a script when a player collides with it.
 * Useful for dynamic events.
 */
public class Trigger extends GameObject {
    private final Runnable run;
    
    public Trigger(Location location, Texture texture, Runnable run) {
        setTexture(texture);
        setLocation(location);
        this.run = run;
    }
    
    @Tick(priority = 3)
    public void tick() {
        if (collidesWith(Player.class)) run.run();
    }
}
