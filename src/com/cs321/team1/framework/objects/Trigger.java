package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.textures.Texture;

public class Trigger extends GameObject implements Runnable {
    private final Runnable run;
    
    public Trigger(Level level, int tileX, int tileY, int width, int height, Runnable run) {
        super(level);
        setTexture(new Texture(width, height));
        getLocation().setTile(tileX, tileY);
        this.run = run;
    }
    
    @Override
    public void run() {
        if (collidesWith(getRoom().getPlayer())) run.run();
    }
}
