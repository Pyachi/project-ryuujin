package com.cs321.team1.util;

public record Vec2(int x, int y) {

  public static Vec2 fromString(String str) {
    String[] args = str.split(":");
    return new Vec2(Integer.parseInt(args[0]), Integer.parseInt(args[1])).toTile();
  }

  public Vec2 add(int x, int y) {
    return new Vec2(this.x + x, this.y + y);
  }

  @Override
  public String toString() {
    return x / 16 + ":" + y / 16;
  }

  public Vec2 toTile() {
    return new Vec2(x * 16, y * 16);
  }
}
