package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Level;

public class ModuloCrate extends Crate {
    public ModuloCrate(Level level, Location loc, int value) {
        super(level, loc, value);
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
        if (crate instanceof IntegerCrate) return crate.getValue() % getValue();
        return 0;
    }
}