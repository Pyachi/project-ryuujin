package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class MultiplyCrate extends Crate {

  public MultiplyCrate(Vec2 loc, int value) {
    super(loc, "crates/mul", value);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return other instanceof IntegerCrate || other instanceof ModuloCrate
        || other instanceof MultiplyCrate;
  }

  @Override
  public Crate getMergedCrate(Vec2 loc, Crate other) {
    if (other instanceof IntegerCrate) {
      return new IntegerCrate(loc, other.getValue() * getValue());
    }
    if (other instanceof ModuloCrate) {
      return new ModuloCrate(loc, other.getValue() * getValue());
    }
    if (other instanceof MultiplyCrate) {
      return new MultiplyCrate(loc, other.getValue() * getValue());
    }
    return null;
  }

  @Override
  public String toString() {
    return "MUL|" + getLocation().toString() + "|" + getValue();
  }
}
