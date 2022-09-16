package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Textures;

public class ScaleCrate extends Crate {
    private final Type type;
    private final int value;
    
    public static ScaleCrate NegateCrate(Room room, Location loc) {
        return new ScaleCrate(room, loc, Type.NEGATE, -1);
    }
    
    public static ScaleCrate ScaleUpCrate(Room room, Location loc, int value) {
        return new ScaleCrate(room, loc, Type.SCALE_UP, Math.max(value,2));
    }
    
    public static ScaleCrate ScaleDownCrate(Room room, Location loc, int value) {
        return new ScaleCrate(room, loc, Type.SCALE_DOWN, Math.max(value,2));
    }
    
    private ScaleCrate(Room room, Location loc, Type type, int value) {
        super(room, loc);
        this.type = type;
        this.value = value;
        setTexture(Textures.CRATE);
    }
    
    @Override
    public void run() {
        super.run();
        if (collidesWith(IntegerCrate.class)) {
            IntegerCrate crate = getCollisions(IntegerCrate.class).get(0);
            int value = crate.getValue();
            switch (this.type) {
                case NEGATE -> value = -value;
                case SCALE_UP -> value *= this.value;
                case SCALE_DOWN -> value /= this.value;
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
        return crate instanceof IntegerCrate && switch (type) {
            case NEGATE, SCALE_UP -> true;
            case SCALE_DOWN -> (((IntegerCrate) crate).getValue() % value == 0);
        };
    }
    
    @Override
    String getString() {
        return switch (type) {
            case NEGATE -> "-";
            case SCALE_UP -> value + "x";
            case SCALE_DOWN -> "1/" + value;
        };
    }
    
    private enum Type {
        NEGATE, SCALE_UP, SCALE_DOWN
    }
}
