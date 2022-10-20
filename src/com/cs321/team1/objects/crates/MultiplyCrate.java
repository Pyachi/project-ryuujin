package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Location;

public class MultiplyCrate extends Crate {
    public MultiplyCrate(Location loc, int value) {
        super(loc, value);
    }

    @Override
    public String getString() {
        return getValue() + "w";
    }

    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplyCrate;
    }

    @Override
    public Crate getMergedCrate(Location loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, crate.getValue() * getValue());
        if (crate instanceof ModuloCrate) return new ModuloCrate(loc, crate.getValue() * getValue());
        if (crate instanceof MultiplyCrate) return new MultiplyCrate(loc, crate.getValue() * getValue());
        return null;
    }

    @Override
    public String toString() {
        return "MUL|" + getLocation().toString() + "|" + getValue();
    }
}
