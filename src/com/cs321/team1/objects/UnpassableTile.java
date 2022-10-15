package com.cs321.team1.objects;

import com.cs321.team1.map.Location;
import com.cs321.team1.assets.Texture;

/**
 * A static object that provides collision detection
 */
public class UnpassableTile extends GameObject {
    public UnpassableTile(Location location, Texture texture, int width, int height) {
        super(width, height);
        setTexture(texture);
        setLocation(location);
    }
    
    public UnpassableTile(Location location, Texture texture) {
        super(texture.width / 16, texture.height / 16);
        setTexture(texture);
        setLocation(location);
    }
    
    public UnpassableTile(Location location, int width, int height) {
        super(width, height);
        setLocation(location);
    }
}
