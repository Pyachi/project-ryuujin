package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Dimension;
import com.cs321.team1.map.Location;

/**
 * A static object that ignores collision
 */
public class PassableTile extends GameObject {
    public PassableTile(Location loc, Dimension size, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(size);
    }

    public PassableTile(Location loc, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(tex.size);
    }

    public PassableTile(Location loc, Dimension size) {
        setLocation(loc);
        setSize(size);
    }

    @Override
    public String toString() {
        return "FLR|" + getLocation().toString() + "|" + getSize().toString() + "|" + getTexture().toString();
    }
}
