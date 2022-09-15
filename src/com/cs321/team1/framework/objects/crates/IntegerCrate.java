package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Textures;

public class IntegerCrate extends Crate   {
    private final int value;
    
    public IntegerCrate(Room room, Location loc, int value) {
        super(room, loc);
        this.value = value;
        setTexture(Textures.CRATE);
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void run() {
        super.run();
        if (collidesWith(IntegerCrate.class)) {
            IntegerCrate crate = getCollisions(IntegerCrate.class).get(0);
            Location location;
            if (Game.getPlayer().getGrabbedCrate() == this) location = crate.getLocation().clone();
            else location = this.getLocation().clone();
            new IntegerCrate(getRoom(), location, crate.getValue() + getValue());
            crate.kill();
            kill();
        }
    }
    
    @Override
    boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate || crate != null && crate.canInteractWith(this);
    }
    
    @Override
    public String getString() {
        return Integer.toString(value);
    }
}
