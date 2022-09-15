package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Textures;
import com.cs321.team1.framework.objects.GameObject;

public class UnpassableTile extends GameObject {
    public UnpassableTile(Room room, int tileX, int tileY, Textures texture) {
        super(room);
        setTexture(texture);
        getLocation().setTile(tileX, tileY);
    }
}
