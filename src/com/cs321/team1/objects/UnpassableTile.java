package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.assets.Texture;

/**
 * A static object that provides collision detection
 * Useful for walls, obstacles, etc.
 */
public class UnpassableTile extends GameObject {
    public UnpassableTile(Location location, Texture texture) {
        setTexture(texture);
        setLocation(location);
    }
}
