package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.textures.Textures;

public class PassableTile extends GameObject {
    public PassableTile(Room room, int tileX, int tileY, Textures texture) {
        super(room);
        setTexture(texture);
        getLocation().setTile(tileX, tileY);
    }
}
