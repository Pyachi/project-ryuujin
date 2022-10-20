package com.cs321.team1.objects.crates;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;

public class UnpoweredCrate extends Crate {
    public UnpoweredCrate(Location loc, int value) {
        super(loc, value);
        setTexture(new Texture("map/floor", 1));
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
    public boolean canInteractWith(Crate crate) {
        return false;
    }

    @Override
    public Crate getMergedCrate(Location location, Crate crate) {
        return null;
    }

    @Override
    public String toString() {
        return "PWR|" + getLocation().toString() + "|" + getValue();
    }
}
