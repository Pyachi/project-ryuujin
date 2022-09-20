package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.textures.Textures;

public class UnpassableTile extends GameObject {
    public UnpassableTile(Level level, int tileX, int tileY, Textures texture) {
        super(level);
        setTexture(texture);
        getLocation().setTile(tileX, tileY);
    }
}
