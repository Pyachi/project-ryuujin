package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.tiles.Door;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

public class IntegerCrate extends Crate implements Runnable {
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
    boolean shouldCollide() {
        return collidesWith(UnpassableTile.class) || collidesWith(Door.class) ||
                getCollisions(ScaleCrate.class).stream().anyMatch(Crate::shouldCollide);
    }
    
    @Override
    public String getString() {
        return Integer.toString(value);
    }
}
