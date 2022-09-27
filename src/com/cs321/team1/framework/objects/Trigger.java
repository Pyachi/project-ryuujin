package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.textures.Texture;

public class Trigger extends GameObject implements Runnable {
    private final Runnable run;
    
    public Trigger(Level level, Location location, Texture texture, Runnable run) {
        super(level);
        setTexture(texture);
        setLocation(location);
        this.run = run;
    }
    
    @Override
    public void run() {
        if (collidesWith(getRoom().getPlayer())) run.run();
    }
}
