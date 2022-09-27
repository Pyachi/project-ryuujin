package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;

public class IntegerCrate extends Crate {
    public IntegerCrate(Level level, Location loc, int value) {
        super(level, loc, value);
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
