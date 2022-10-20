package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class NegateCrate extends Crate {
    public NegateCrate(Vec2 loc) {
        super(loc, -1);
    }
    
    @Override
    public String getString() {
        return "-";
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, crate.getValue() * getValue());
        return null;
    }
    
    @Override
    public String toString() {
        return "NEG|" + getLocation().toString();
    }
}
