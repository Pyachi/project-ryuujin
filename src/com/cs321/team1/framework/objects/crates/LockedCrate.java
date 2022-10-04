package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.textures.Textures;

public class LockedCrate extends Crate {
    public LockedCrate(Location loc, int value) {
        super(loc, value);
        setTexture(Textures.BOULDER.get());
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
    public Crate getMergedCrate(Location location, Crate crate) {
        return null;
    }
}
