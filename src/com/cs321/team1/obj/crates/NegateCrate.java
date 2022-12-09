package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class NegateCrate extends Crate {

  public NegateCrate(Vec2 loc) {
    super(loc, "crates/neg", -1);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return other instanceof IntegerCrate;
  }

  @Override
  public Crate getMergedCrate(Vec2 loc, Crate other) {
    if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() * getValue());
    return null;
  }

  @Override
  public String getString() {
    return "-";
  }

  @Override
  public String toString() {
    return "NEG|" + getLocation().toString();
  }
}
