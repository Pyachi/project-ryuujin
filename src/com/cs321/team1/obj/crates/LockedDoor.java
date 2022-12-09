package com.cs321.team1.obj.crates;

import com.cs321.team1.util.Vec2;

public class LockedDoor extends Crate {

  public LockedDoor(Vec2 loc, int value) {
    super(loc, "crates/lck", value);
  }

  @Override
  public boolean isMovable() {
    return false;
  }

  @Override
  public boolean canBeAppliedTo(Crate other) {
    return false;
  }

  @Override
  public Crate getMergedCrate(Vec2 location, Crate other) {
    return null;
  }

  @Override
  public String toString() {
    return "LCK|" + getLocation().toString() + "|" + getValue();
  }
}
