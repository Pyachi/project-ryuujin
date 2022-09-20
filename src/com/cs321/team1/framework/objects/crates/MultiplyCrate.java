package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Level;

public class MultiplyCrate extends Crate {
    public MultiplyCrate(Level level, Location loc, int value) {
        super(level, loc, value);
    }
    
    @Override
    public String getString() {
        return getValue() + "x";
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplyCrate;
    }
    
    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplyCrate)
            return crate.getValue() * getValue();
        return 0;
    }
}
