package com.cs321.team1.map;

import java.awt.Dimension;

public class Vec2 {

  public final int x;
  public final int y;

  public Vec2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Vec2 fromDimension(Dimension dim) {
    return new Vec2(dim.width, dim.height);
  }

  public static Vec2 fromString(String str) {
    String[] args = str.split(":");
    Vec2 vec2 = new Vec2(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    return args[0].equals("T") ? vec2.toTile() : vec2;
  }

  public Vec2 add(Vec2 other) {
    return new Vec2(x + other.x, y + other.y);
  }

  public Dimension toDimension() {
    return new Dimension(x, y);
  }

  @Override
  public String toString() {
    return (x % 16 == 0 && y % 16 == 0) ? "T:" + x / 16 + ":" + y / 16 : "R:" + x + ":" + y;
  }

  public Vec2 toTile() {
    return new Vec2(x * 16, y * 16);
  }
}
