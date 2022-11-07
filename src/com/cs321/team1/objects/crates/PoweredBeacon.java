package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

/**
 * Object for the level completion condition. Once all beacons are powered, the level is complete
 */
public class PoweredBeacon extends Crate {
    /**
     * Creates a powered beacon at the given location
     *
     * @param loc The location of the beacon
     */
    public PoweredBeacon(Vec2 loc) {
        super(loc, "crates/pwr", 0);
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
        return "";
    }
    
    @Override
    public String toString() {
        return "BEA|" + getLocation().toString();
    }
}
