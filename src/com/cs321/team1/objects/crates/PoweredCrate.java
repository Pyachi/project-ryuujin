package com.cs321.team1.objects.crates;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

public class PoweredCrate extends Crate {
    public PoweredCrate(Vec2 loc) {
        super(loc, 0);
        setTexture(new Texture("crates/interactable", 1));
    }
    
    @Override
    public boolean canGrab() {
        return false;
    }
    
    @Override
    public String getString() {
        return "";
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
        return "BEA|" + getLocation().toString();
    }
}
