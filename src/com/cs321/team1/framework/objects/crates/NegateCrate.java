package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;

public class NegateCrate extends Crate {
    public NegateCrate(Location loc) {
        super(loc, -1);
    }
    
    @Override
    public String getString() {
        return "-";
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate;
    }
    
    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate) return crate.getValue() * getValue();
        return 0;
    }
}
