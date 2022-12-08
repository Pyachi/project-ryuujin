package com.cs321.team1.assets.audio.filters;

public enum Filters {
  MUFFLE(new MuffleFilter()),
  ECHO(new EchoFilter());
  public final Filter filter;

  Filters(Filter filter) {
    this.filter = filter;
  }
}
