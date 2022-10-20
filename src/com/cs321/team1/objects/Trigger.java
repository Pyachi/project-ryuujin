package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Dimension;
import com.cs321.team1.map.Location;

/**
 * A static tile that runs a script when a player collides with it
 * Useful for dynamic events
 */
public class Trigger extends GameObject {
    private final Runnable run;

    public Trigger(Location loc, Dimension size, Texture tex, Runnable run) {
        setLocation(loc);
        setSize(size);
        setTexture(tex);
        this.run = run;
    }

    public Trigger(Location loc, Texture tex, Runnable run) {
        setTexture(tex);
        setSize(tex.size);
        setLocation(loc);
        this.run = run;
    }

    public Trigger(Location location, Dimension size, Runnable run) {
        setLocation(location);
        setSize(size);
        this.run = run;
    }

    @Tick(priority = 3)
    public void run() {
        if (collidesWith(Player.class)) run.run();
    }

    @Override
    public String toString() {
        return "NULL|" + getLocation().toString();
    }
}
