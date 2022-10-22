package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class MultiplyCrate extends Crate {
    public MultiplyCrate(Vec2 loc, int value) {
        super(loc,"crates/crate", value);
    }
    
    @Override
    public String getString() {
        return getValue() + "w";
    }
    
    @Override
    public boolean canBeAppliedTo(Crate other) {
        return other instanceof IntegerCrate || other instanceof ModuloCrate || other instanceof MultiplyCrate;
    }
    
    @Override
    public Crate getMergedCrate(Vec2 loc, Crate other) {
        if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() * getValue());
        if (other instanceof ModuloCrate) return new ModuloCrate(loc, other.getValue() * getValue());
        if (other instanceof MultiplyCrate) return new MultiplyCrate(loc, other.getValue() * getValue());
        return null;
    }
    
    @Override
    public String toString() {
        return "MUL|" + getLocation().toString() + "|" + getValue();
    }
}
