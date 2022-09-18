package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;

public class DivisionCrate extends Crate {
    public DivisionCrate(Room room, Location loc, int value) {
        super(room, loc, value);
    }
    
    @Override
    public String getString() {
        return "1/" + getValue();
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return (crate instanceof IntegerCrate || crate instanceof ModuloCrate ||
                crate instanceof MultiplicationCrate) && crate.getValue() % getValue() == 0 ||
                crate instanceof DivisionCrate;
    }
    
    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplicationCrate)
            return crate.getValue() / getValue();
        if (crate instanceof DivisionCrate) return crate.getValue() * getValue();
        return 0;
    }
}
