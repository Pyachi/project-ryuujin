package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Location;

public class ModuloCrate extends Crate {
    public ModuloCrate(Location loc, int value) {
        super(loc, value);
    }
    
    @Override
    public String getString() {
        return "%" + getValue();
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate;
    }
    
    @Override
    public Crate getMergedCrate(Location loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, Math.floorMod(crate.getValue(), getValue()));
        return null;
    }
}