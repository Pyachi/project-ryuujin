package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class UnpoweredCrate extends Crate {
    public UnpoweredCrate(Vec2 loc, int value) {
        super(loc,"map/floor", value);
    }
    
    @Override
    public boolean canGrab() {
        return false;
    }
    
    @Override
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return false;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 location, Crate other) {
        return null;
    }
    
    @Override
    public String toString() {
        return "PWR|" + getLocation().toString() + "|" + getValue();
    }
}
