package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.GameObject;

public class PassableTile extends GameObject {
    public PassableTile(int locX, int locY, Textures texture) {
        this.texture = texture;
        setTilePosition(locX, locY);
    }
}
