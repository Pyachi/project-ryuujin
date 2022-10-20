package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class ModuloCrate extends Crate {
    public ModuloCrate(Vec2 loc, int value) {
        super(loc, value);
    }
    
    @Override
    public String getString() {
        return "%" + getValue();
    }
    
    @Override
    public boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate crate) {
        if (crate instanceof IntegerCrate) return new IntegerCrate(loc, Math.floorMod(crate.getValue(), getValue()));
        return null;
    }
    
    @Override
    public String toString() {
        return "MOD|" + getLocation().toString() + "|" + getValue();
    }
}