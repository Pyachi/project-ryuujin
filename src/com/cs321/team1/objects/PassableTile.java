package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

/**
 * GameObject for tiles that can be passed through
 */
public class PassableTile extends GameObject {
    /**
     * Creates a PassableTile with the given characteristics
     *
     * @param loc  Location of tile
     * @param size Size of tile
     * @param tex  Texture of tile
     */
    public PassableTile(Vec2 loc, Vec2 size, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(size);
    }
    
    /**
     * Creates a PassableTile with the given characteristics
     *
     * @param loc Location of tile
     * @param tex Texture of tile
     */
    public PassableTile(Vec2 loc, Texture tex) {
        setTexture(tex);
        setLocation(loc);
        setSize(tex.size);
    }
    
    /**
     * Creates a PassableTile with the given characteristics
     *
     * @param loc  Location of tile
     * @param size Size of tile
     */
    public PassableTile(Vec2 loc, Vec2 size) {
        setLocation(loc);
        setSize(size);
    }
    
    @Override
    public String toString() {
        return "FLR|" + getLocation().toString() + "|" + getSize().toString() + "|" + getTexture().toString();
    }
}
