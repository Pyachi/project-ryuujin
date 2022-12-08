package com.cs321.team1.objects.crates;

import com.cs321.team1.map.Vec2;


public class UnpoweredBeacon extends Crate {

  public UnpoweredBeacon(Vec2 loc, int value) {
    super(loc, "crates/upwr", value);
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return false;
  }

  @Override
  public boolean canGrab() {
    return false;
  }

  @Override
  public Crate getMergedCrate(Vec2 location, Crate other) {
    return null;
  }

  @Override
  public String toString() {
    return "PWR|" + getLocation().toString() + "|" + getValue();
  }
}
