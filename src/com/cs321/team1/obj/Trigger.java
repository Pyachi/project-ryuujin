package com.cs321.team1.obj;

import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;

public class Trigger extends GameObject {

  private final Runnable run;
  private final String command;

  public Trigger(Vec2 loc, Vec2 size, Texture tex, String command) {
    setLocation(loc);
    setSize(size);
    if (tex != null) {
      setTexture(tex);
    }
    this.command = command;
    run = getCommand();
    registerTick(2, this::tick);
  }

  public Trigger(Vec2 loc, Texture tex, String command) {
    this(loc, tex.size, tex, command);
  }

  public Trigger(Vec2 location, Vec2 size, String command) {
    this(location, size, null, command);
  }

  private void tick() {
    if (collidesWith(Player.class)) {
      run.run();
    }
  }

  @Override
  public String toString() {
    return "TGR|" + getLocation() + "|" + getSize() + "|" + getTexture() + "->" + command;
  }

  private Runnable getCommand() {
    String[] cmd = command.split("\\|");
    return switch (cmd[0]) {
      case "LVL" -> () -> {
        if (Controls.SELECT.isPressed()) {
          Level lvl = Level.load(cmd[1]);
          if (lvl != null) {
            Game.get().pushSegments(new LevelTransition(Game.get().getHighestSegment(), lvl), lvl);
          }
        }
      };
      default -> () -> {
      };
    };
  }
}
