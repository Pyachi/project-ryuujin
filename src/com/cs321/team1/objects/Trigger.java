package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;

/**
 * A static tile that runs a script when a player collides with it
 * Useful for dynamic events
 */
public class Trigger extends GameObject {
    private final Runnable run;
    
    public Trigger(Location location, Texture texture, int width, int height, Runnable run) {
        super(width, height);
        setTexture(texture);
        setLocation(location);
        this.run = run;
    }
    
    public Trigger(Location location, Texture texture, Runnable run) {
        super(texture.width / 16, texture.height / 16);
        setTexture(texture);
        setLocation(location);
        this.run = run;
    }
    
    public Trigger(Location location, int width, int height, Runnable run) {
        super(width, height);
        setLocation(location);
        this.run = run;
    }
    
    @Tick(priority = 3)
    public void run() {
        if (collidesWith(Player.class)) run.run();
    }
}
