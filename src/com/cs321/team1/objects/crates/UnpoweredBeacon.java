package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Object for the level completion condition. Once all beacons are powered, the level is complete
 */
public class UnpoweredBeacon extends Crate {
    /**
     * Creates an unpowered beacon at the given location with the given power value
     *
     * @param loc   The location of the beacon
     * @param value The power value of the beacon
     */
    public UnpoweredBeacon(Vec2 loc, int value) {
        super(loc, "map/floor", value);
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
    public String getString() {
        return Integer.toString(getValue());
    }
    
    @Override
    public String toString() {
        return "PWR|" + getLocation().toString() + "|" + getValue();
    }
}
