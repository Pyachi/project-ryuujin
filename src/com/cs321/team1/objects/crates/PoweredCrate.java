package com.cs321.team1.objects.crates;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;

public class PoweredCrate extends Crate {
    public PoweredCrate(Location loc) {
        super(loc, 0);
        setTexture(new Texture("crates/interactable",1));
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
    public Crate getMergedCrate(Location location, Crate crate) {
        return null;
    }
}
