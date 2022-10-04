package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;

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
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate) return Math.floorMod(crate.getValue(), getValue());
        return 0;
    }
}