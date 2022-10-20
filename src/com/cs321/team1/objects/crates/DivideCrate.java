package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Location;

public class DivideCrate extends Crate {
    public DivideCrate(Location loc, int value) {
        super(loc, value);
    }

    @Override
    public String getString() {
        return "1/" + getValue();
    }

    @Override
    public boolean canInteractWith(Crate crate) {
        return (crate instanceof IntegerCrate || crate instanceof ModuloCrate || crate instanceof MultiplyCrate)
                && crate.getValue() % getValue() == 0 || crate instanceof DivideCrate;
    }

    @Override
    public Crate getMergedCrate(Location loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, crate.getValue() / getValue());
        if (crate instanceof ModuloCrate) return new ModuloCrate(loc, crate.getValue() / getValue());
        if (crate instanceof MultiplyCrate) return new MultiplyCrate(loc, crate.getValue() / getValue());
        if (crate instanceof DivideCrate) return new DivideCrate(loc, crate.getValue() * getValue());
        return null;
    }

    @Override
    public String toString() {
        return "DIV|" + getLocation().toString() + "|" + getValue();
    }
}
