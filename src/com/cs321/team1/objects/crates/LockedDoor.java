package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Object that behaves like a wall until the given integer value is merged with it
 */
public class LockedDoor extends Crate {
    /**
     * Creates a locked door at the given location with the given value
     *
     * @param loc   The location of the door
     * @param value The value of the door lock
     */
    public LockedDoor(Vec2 loc, int value) {
        super(loc, "crates/lck", value);
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return false;
    }
    
    @Override
    public boolean canGrab() {
        return false;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 location, Crate other) {
        return null;
    }
    
    @Override
    public String toString() {
        return "LCK|" + getLocation().toString() + "|" + getValue();
    }
}
