package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;

public class MultiplicationCrate extends Crate {
    public MultiplicationCrate(Room room, Location loc, int value) {
        super(room, loc, value);
    }
    
    @Override
    public String getString() {
        return getValue() + "x";
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplicationCrate;
    }
    
    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplicationCrate)
            return crate.getValue() * getValue();
        return 0;
    }
}
