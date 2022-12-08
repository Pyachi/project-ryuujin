package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;

public class ModuloCrate extends Crate {

  public ModuloCrate(Vec2 loc, int value) {
    super(loc, "crates/mod", value);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return other instanceof IntegerCrate;
  }

  @Override
  public Crate getMergedCrate(Vec2 loc, Crate other) {
    if (other instanceof IntegerCrate) {
      return new IntegerCrate(loc, Math.floorMod(other.getValue(), getValue()));
    }
    return null;
  }

  @Override
  public String toString() {
    return "MOD|" + getLocation().toString() + "|" + getValue();
  }
}