package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.GameObject;

public class UnpassableTile extends GameObject {
    public UnpassableTile(int locX, int locY, int renderPriority, Textures texture) {
        super(renderPriority);
        this.texture = texture;
        setTilePosition(locX, locY);
    }
}
