package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Textures;
import com.cs321.team1.framework.objects.GameObject;

public class UnpassableTile extends GameObject {
    private final Textures texture;

    public UnpassableTile(int locX, int locY, Textures texture) {
        this.texture = texture;
        setTilePosition(locX, locY);
    }

    @Override
    protected Textures getTexture() {
        return texture;
    }
}
