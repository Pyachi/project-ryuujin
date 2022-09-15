package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.tiles.Door;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

public class ScaleCrate extends Crate {
    private final Values value;
    
    public ScaleCrate(Room room, Location loc, Values value) {
        super(room, loc);
        this.value = value;
        setTexture(Textures.CRATE);
    }
    
    @Override
    public void run() {
        super.run();
        if (collidesWith(IntegerCrate.class)) {
            IntegerCrate crate = getCollisions(IntegerCrate.class).get(0);
            int value = crate.getValue();
            switch (this.value) {
                case NEGATE -> value = -value;
                case DOUBLE -> value *= 2;
                case TRIPLE -> value *= 3;
                case HALF -> value /= 2;
                case QUARTER -> value /= 4;
            }
            Location location;
            if (Game.getPlayer().getGrabbedCrate() == this) location = crate.getLocation().clone();
            else location = this.getLocation().clone();
            new IntegerCrate(getRoom(), location, value);
            crate.kill();
            kill();
        }
    }
    
    @Override
    boolean canInteractWith(Crate crate) {
        return crate instanceof IntegerCrate && switch (value) {
            case NEGATE, DOUBLE, TRIPLE -> true;
            case HALF -> (((IntegerCrate) crate).getValue() % 2 == 0);
            case QUARTER -> (((IntegerCrate) crate).getValue() % 4 == 0);
        };
    }
    
    @Override
    String getString() {
        return switch (value) {
            case NEGATE -> "-";
            case DOUBLE -> "2x";
            case TRIPLE -> "3x";
            case HALF -> "1/2";
            case QUARTER -> "1/4";
        };
    }
    
    public enum Values {
        NEGATE, DOUBLE, TRIPLE, HALF, QUARTER
    }
}
