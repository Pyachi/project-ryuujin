package com.cs321.team1.framework.objects;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

public class Trigger extends GameObject implements Runnable {
    private final Runnable run;
    
    public Trigger(Room room, int tileX, int tileY, int width, int height, Runnable run) {
        super(room);
        setTexture(new Texture(Textures.WALL_TILE, width, height));
        getLocation().setTile(tileX, tileY);
        this.run = run;
    }
    
    @Override
    public void run() {
        if (collidesWith(Game.getPlayer())) run.run();
    }
}
