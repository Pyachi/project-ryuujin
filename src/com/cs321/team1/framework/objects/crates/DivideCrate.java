package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;

public class DivideCrate extends Crate {
    public DivideCrate(Level level, Location loc, int value) {
        super(level, loc, value);
    }

    @Override
    public String getString() {
        return "1/" + getValue();
    }

    @Override
    public boolean canInteractWith(Crate crate) {
        return (crate instanceof IntegerCrate || crate instanceof ModuloCrate ||
                crate instanceof MultiplyCrate) && crate.getValue() % getValue() == 0 ||
                crate instanceof DivideCrate;
    }

    @Override
    public int getMergedValue(Crate crate) {
        if (crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplyCrate)
            return crate.getValue() / getValue();
        if (crate instanceof DivideCrate) return crate.getValue() * getValue();
        return 0;
    }
}
