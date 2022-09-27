package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.textures.Textures;

public class PassableTile extends GameObject {
    public PassableTile(Level level, Location location, Textures texture) {
        super(level);
        setTexture(texture);
        setLocation(location);
    }
}
