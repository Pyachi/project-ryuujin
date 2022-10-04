package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.textures.Texture;

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
