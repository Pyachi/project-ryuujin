package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Location;

public class IntegerCrate extends Crate {
    public IntegerCrate(Location loc, int value) {
        super(loc, value);
    }
    
    @Override
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate
                || (crate instanceof LockedCrate || crate instanceof UnpoweredCrate) && crate.getValue() == getValue();
    }
    
    @Override
    public Crate getMergedCrate(Location loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, crate.getValue() + getValue());
        if (crate instanceof UnpoweredCrate) return new PoweredCrate(loc);
        return null;
    }
}