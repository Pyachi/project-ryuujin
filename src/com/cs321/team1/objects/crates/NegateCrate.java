package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Crate that negates the value of other crates
 */
public class NegateCrate extends Crate {
    /**
     * Creates a negation crate at the given location
     *
     * @param loc The location of the crate
     */
    public NegateCrate(Vec2 loc) {
        super(loc, "crates/negation", -1);
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return other instanceof IntegerCrate;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate other) {
        if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() * getValue());
        return null;
    }
    
    @Override
    public String getString() {
        return "-";
    }
    
    @Override
    public String toString() {
        return "NEG|" + getLocation().toString();
    }
}
