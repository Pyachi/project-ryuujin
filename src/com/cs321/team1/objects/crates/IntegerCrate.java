package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class IntegerCrate extends Crate {
    public IntegerCrate(Vec2 loc, int value) {
        super(loc,"crates/integer", value);
    }
    
    @Override
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return other instanceof IntegerCrate ||
                (other instanceof LockedCrate || other instanceof UnpoweredCrate) && other.getValue() == getValue();
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate other) {
        if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() + getValue());
        if (other instanceof UnpoweredCrate) return new PoweredCrate(loc);
        return null;
    }
    
    @Override
    public String toString() {
        return "INT|" + getLocation().toString() + "|" + getValue();
    }
}
