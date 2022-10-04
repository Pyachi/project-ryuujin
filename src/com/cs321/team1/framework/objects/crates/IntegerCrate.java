package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;

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
        return crate instanceof IntegerCrate;
    }
    
    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate) return getValue() + crate.getValue();
        return 0;
    }
}
