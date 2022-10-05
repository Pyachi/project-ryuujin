package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.assets.Texture;

/**
 * A static object that ignores collision.
 * Useful for floors, decoration, etc.
 */
public class PassableTile extends GameObject {
    public PassableTile(Location location, Texture texture) {
        setTexture(texture);
        setLocation(location);
    }
}
