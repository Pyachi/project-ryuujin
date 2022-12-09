package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class DivideCrate extends Crate {

  public DivideCrate(Vec2 loc, int value) {
    super(loc, "crates/div", value);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return (other instanceof IntegerCrate || other instanceof ModuloCrate
        || other instanceof MultiplyCrate) && other.getValue() % getValue() == 0
        || other instanceof DivideCrate;
  }

  @Override
  public Crate getMergedCrate(Vec2 loc, Crate other) {
    if (other instanceof IntegerCrate) return new IntegerCrate(loc, other.getValue() / getValue());
    if (other instanceof ModuloCrate) return new ModuloCrate(loc, other.getValue() / getValue());
    if (other instanceof MultiplyCrate)
      return new MultiplyCrate(loc, other.getValue() / getValue());
    if (other instanceof DivideCrate) return new DivideCrate(loc, other.getValue() * getValue());
    return null;
  }

  @Override
  public String toString() {
    return "DIV|" + getLocation().toString() + "|" + getValue();
  }
}
