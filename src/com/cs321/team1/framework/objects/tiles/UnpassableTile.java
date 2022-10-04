package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.textures.Texture;
import com.cs321.team1.framework.textures.Textures;

public class UnpassableTile extends GameObject {
    public UnpassableTile(Location location, Texture texture) {
        setTexture(texture);
        setLocation(location);
    }
}
