package com.cs321.team1.objects.crates;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

public class LockedCrate extends Crate {
    public LockedCrate(Vec2 loc, int value) {
        super(loc, value);
        setTexture(new Texture("map/tile", 1));
    }
    
    @Override
    public boolean canGrab() {
        return false;
    }
    
    @Override
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return false;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 location, Crate crate) {
        return null;
    }
    
    @Override
    public String toString() {
        return "LCK|" + getLocation().toString() + "|" + getValue();
    }
}
