package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Crate that modulates the values of other crates
 */
public class ModuloCrate extends Crate {
    /**
     * Creates a Modulation crate at the given location with the given value
     *
     * @param loc   The location of the crate
     * @param value The value of the crate
     */
    public ModuloCrate(Vec2 loc, int value) {
        super(loc, "crates/mod", value);
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return other instanceof IntegerCrate;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate other) {
        if (other instanceof IntegerCrate) return new IntegerCrate(loc, Math.floorMod(other.getValue(), getValue()));
        return null;
    }
    
    @Override
    public String toString() {
        return "MOD|" + getLocation().toString() + "|" + getValue();
    }
}