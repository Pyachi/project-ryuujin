package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Crate that simply stores an integer value
 */
public class IntegerCrate extends Crate {
    /**
     * Creates an Integer Crate at the given location with the given value
     *
     * @param loc   The location of the crate
     * @param value The value of the crate
     */
    public IntegerCrate(Vec2 loc, int value) {
        super(loc, "crates/integer", value);
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return other instanceof IntegerCrate ||
                (other instanceof LockedDoor || other instanceof UnpoweredBeacon) && other.getValue() == getValue();
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate other) {
        if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() + getValue());
        if (other instanceof UnpoweredBeacon) return new PoweredBeacon(loc);
        return null;
    }
    
    @Override
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public String toString() {
        return "INT|" + getLocation().toString() + "|" + getValue();
    }
}
