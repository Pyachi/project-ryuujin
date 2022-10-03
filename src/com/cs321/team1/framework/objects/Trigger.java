package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.intr.Tick;
import com.cs321.team1.framework.objects.intr.Tickable;
import com.cs321.team1.framework.textures.Texture;

public class Trigger extends GameObject implements Tickable {
    private final Runnable run;

    public Trigger(Level level, Location location, Texture texture, Runnable run) {
        super(level);
        setTexture(texture);
        setLocation(location);
        this.run = run;
    }

    @Tick(priority = 2)
    public void tick() {
        if (collidesWith(Player.class)) run.run();
    }
}
