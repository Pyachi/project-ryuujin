package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class PoweredBeacon extends Crate {

  public PoweredBeacon(Vec2 loc) {
    super(loc, "crates/pwr", 0);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return false;
  }

  @Override
  public boolean isMovable() {
    return false;
  }

  @Override
  public Crate getMergedCrate(Vec2 location, Crate other) {
    return null;
  }

  @Override
  public String getString() {
    return "";
  }

  @Override
  public String toString() {
    return "BEA|" + getLocation().toString();
  }
}
