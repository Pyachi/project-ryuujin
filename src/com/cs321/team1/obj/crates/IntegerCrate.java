package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class IntegerCrate extends Crate {

  public IntegerCrate(Vec2 loc, int value) {
    super(loc, "crates/int", value);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return other instanceof IntegerCrate
        || (other instanceof LockedDoor || other instanceof UnpoweredBeacon)
        && other.getValue() == getValue();
  }

  @Override
  public Crate getMergedCrate(Vec2 loc, Crate other) {
    if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() + getValue());
    if (other instanceof UnpoweredBeacon) return new PoweredBeacon(loc);
    return null;
  }

  @Override
  public String toString() {
    return "INT|" + getLocation().toString() + "|" + getValue();
  }
}
