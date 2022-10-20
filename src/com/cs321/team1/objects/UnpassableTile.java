package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Dimension;
import com.cs321.team1.map.Location;

/**
 * A static object that provides collision detection
 */
public class UnpassableTile extends GameObject {
    public UnpassableTile(Location loc, Dimension size, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(size);
    }

    public UnpassableTile(Location loc, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(tex.size);
    }

    public UnpassableTile(Location loc, Dimension size) {
        setLocation(loc);
        setSize(size);
    }

    @Override
    public String toString() {
        return "WAL|" + getLocation().toString() + "|" + getSize().toString() + "|" + getTexture().toString();
    }
}
