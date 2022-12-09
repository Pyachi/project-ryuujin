package com.cs321.team1.map;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.obj.Conveyor;
import com.cs321.team1.obj.GameObject;
import com.cs321.team1.obj.PassableTile;
import com.cs321.team1.obj.Player;
import com.cs321.team1.obj.Trigger;
import com.cs321.team1.obj.UnpassableTile;
import com.cs321.team1.obj.crates.DivideCrate;
import com.cs321.team1.obj.crates.IntegerCrate;
import com.cs321.team1.obj.crates.LockedDoor;
import com.cs321.team1.obj.crates.ModuloCrate;
import com.cs321.team1.obj.crates.MultiplyCrate;
import com.cs321.team1.obj.crates.NegateCrate;
import com.cs321.team1.obj.crates.UnpoweredBeacon;
import com.cs321.team1.obj.world.LevelObject;
import com.cs321.team1.obj.world.NavPath;
import com.cs321.team1.obj.world.Navigator;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.ResourceUtil;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.Vec2;
import com.cs321.team1.util.audio.Music;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Level implements GameSegment {

  public final String name;
  public final Vec2 size;
  public final boolean isWorld;
  private final Map<Integer, GameObject> objs = new HashMap<>();
  private final Map<String, List<String>> cmds = new HashMap<>();
  private final boolean complete = false;
  private Music music;

  public Level(Vec2 size, String name, boolean isWorld) {
    this.size = size;
    this.name = name;
    this.isWorld = isWorld;
    addBase();
  }

  public static Level fromString(String lvl) {
    var lines = Arrays.stream(lvl.replaceAll("\r", "").split("\n"))
        .filter(it -> !it.startsWith("//")).toArray(String[]::new);
    var set = lines[0].split("\\|");
    var level = new Level(Vec2.fromString(set[1]), set[2], Boolean.parseBoolean(set[3]));
    for (int i = 1; i < lines.length; i++) {
      level.parseCommand(lines[i]);
    }
    return level;
  }

  public static Level load(String name) {
    try {
      return Level.fromString(String.join("\n", new BufferedReader(new InputStreamReader(
          ResourceUtil.loadStream("resources/levels/" + name + ".ryu"))).lines()
          .toArray(String[]::new)));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void addObject(GameObject obj) {
    obj.setLevel(this);
    if (obj.getID() == 0) {
      obj.setId(getNextID());
    }
    objs.put(obj.getID(), obj);
  }

  public void removeObject(GameObject obj) {
    objs.remove(obj.getID());
  }

  public void addObject(int id, GameObject obj) {
    if (id != -1) {
      obj.setId(id);
      if (objs.containsKey(id)) {
        removeObject(objs.get(id));
      }
    }
    addObject(obj);
  }

  public void removeObject(int id) {
    objs.remove(id);
  }

  public Set<GameObject> getObjects() {
    return new HashSet<>(objs.values());
  }

  public int getScale() {
    int scale = 20;
    var screenSize = Game.get().settings.getScreenSize();
    while (scale * size.x() > screenSize.x() || scale * size.y() > screenSize.y()) {
      scale--;
    }
    return scale;
  }

  @Override
  public void start() {
    if (music != null) {
      music.play();
    }
  }

  @Override
  public void restart() {
    start();
  }

  @Override
  public void update() {
    getObjects().forEach(GameObject::age);

    Map<Integer, List<Runnable>> ticks = new HashMap<>();
    getObjects().forEach(it -> it.getTicks()
        .forEach((key, list) -> ticks.computeIfAbsent(key, ArrayList::new).addAll(list)));
    ticks.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
        .map(Map.Entry::getValue).forEach(it -> it.forEach(Runnable::run));

    if (Controls.BACK.isPressed()) {
      Game.get().pushSegment(new LevelMenu());
    }
    if (!isWorld && getObjects().stream().noneMatch(UnpoweredBeacon.class::isInstance)) {
      Game.get().pushSegment(new LevelCompletion(this));
      Game.get().completeLevel(name);
    }
    cmds.forEach((condition, commandList) -> new ArrayList<>(commandList).forEach(command -> {
      if (checkCMD(condition, command)) {
        commandList.remove(command);
      }
    }));
  }

  @Override
  public void render(Graphics2D buffer) {
    var list = getObjects().stream().filter(it -> it.getTexture() != null)
        .sorted(Comparator.comparingInt(it -> it.getTexture().priority)).toList();
    var image = ResourceUtil.createImage(getScale() * size.x(), getScale() * size.y());
    var graphics = image.createGraphics();
    list.forEach(it -> it.paint(graphics));
    buffer.setColor(Color.BLACK);
    buffer.fillRect(0, 0, Game.get().settings.getScreenSize().x(),
        Game.get().settings.getScreenSize().y());
    buffer.drawImage(image, (Game.get().settings.getScreenSize().x() - image.getWidth()) / 2,
        (Game.get().settings.getScreenSize().y() - image.getHeight()) / 2, null);
    image.flush();
  }

  @Override
  public String toString() {
    removeBase();
    var start = "SET|" + size.toString() + "|" + name + "|" + isWorld + "\n";
    var builder = new StringBuilder();
    if (music != null) {
      builder.append("MSC|").append(music.name()).append("\n");
    }
    cmds.forEach((condition, commandList) -> commandList.forEach(
        command -> builder.append("CMD|").append(condition).append("->").append(command)
            .append("\n")));
    getObjects().forEach(it -> builder.append(it.toString()).append("\n"));
    addBase();
    return start + builder;
  }

  private void addBase() {
    addObject(1, new PassableTile(new Vec2(1, 1).toTile(), size, new Texture("map/base", -1)));
    addObject(2, new UnpassableTile(new Vec2(1, 0).toTile(), new Vec2(size.x(), 1).toTile()));
    addObject(3, new UnpassableTile(new Vec2(1, size.y() / 16 + 1).toTile(),
        new Vec2(size.x(), 1).toTile()));
    addObject(4, new UnpassableTile(new Vec2(0, 1).toTile(), new Vec2(1, size.y()).toTile()));
    addObject(5, new UnpassableTile(new Vec2(size.x() / 16 + 1, 1).toTile(),
        new Vec2(1, size.y()).toTile()));
  }

  private void removeBase() {
    for (int i = 1; i <= 5; i++) {
      removeObject(i);
    }
  }

  private void parseCommand(String cmd) {
    try {
      var line = cmd.split("\\|");
      switch (line[0]) {
        case "MSC" -> music = Music.valueOf(line[1]);
        case "CMD" -> {
          var condition = cmd.split("->")[0].replaceAll("CMD\\|", "");
          var command = cmd.split("->")[1];
          cmds.computeIfAbsent(condition, it -> new ArrayList<>()).add(command);
        }
        default -> {
          int id = -1;
          if (line[0].contains("#")) {
            id = Integer.parseInt(line[0].split("#")[0]);
            line[0] = line[0].split("#")[1];
          }
          var loc = Vec2.fromString(line[1]);
          switch (line[0]) {
            case "NAV" -> addObject(id, new Navigator(loc));
            case "PTH" -> addObject(id, new NavPath(loc));
            case "PLR" -> addObject(id, new Player(loc));
            case "LVL" -> addObject(id, new LevelObject(loc, line[2]));
            case "INT" -> addObject(id, new IntegerCrate(loc, Integer.parseInt(line[2])));
            case "NEG" -> addObject(id, new NegateCrate(loc));
            case "MOD" -> addObject(id, new ModuloCrate(loc, Integer.parseInt(line[2])));
            case "MUL" -> addObject(id, new MultiplyCrate(loc, Integer.parseInt(line[2])));
            case "DIV" -> addObject(id, new DivideCrate(loc, Integer.parseInt(line[2])));
            case "LCK" -> addObject(id, new LockedDoor(loc, Integer.parseInt(line[2])));
            case "PWR" -> addObject(id, new UnpoweredBeacon(loc, Integer.parseInt(line[2])));
            case "CVR" -> addObject(id, switch (line[2]) {
              default -> Conveyor.UP(loc);
              case "D" -> Conveyor.DOWN(loc);
              case "L" -> Conveyor.LEFT(loc);
              case "R" -> Conveyor.RIGHT(loc);
            });
            case "FLR" -> {
              if (line[2].contains("/")) {
                addObject(id, new PassableTile(loc, Texture.fromString(line[2])));
              } else if (line.length == 4) {
                addObject(id,
                    new PassableTile(loc, Vec2.fromString(line[2]), Texture.fromString(line[3])));
              } else {
                addObject(id, new PassableTile(loc, Vec2.fromString(line[2])));
              }
            }
            case "WAL" -> {
              if (line[2].contains("/")) {
                addObject(id, new UnpassableTile(loc, Texture.fromString(line[2])));
              } else if (line.length == 4) {
                addObject(id,
                    new UnpassableTile(loc, Vec2.fromString(line[2]), Texture.fromString(line[3])));
              } else {
                addObject(id, new UnpassableTile(loc, Vec2.fromString(line[2])));
              }
            }
            case "TGR" -> {
              var comm = cmd.split("->")[1];
              line = cmd.split("->")[0].split("\\|");
              if (line[2].contains("/")) {
                addObject(id, new Trigger(loc, Texture.fromString(line[2]), comm));
              } else if (line.length == 4) {
                addObject(id,
                    new Trigger(loc, Vec2.fromString(line[2]), Texture.fromString(line[3]), comm));
              } else {
                addObject(id, new Trigger(loc, Vec2.fromString(line[2]), comm));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean checkCMD(String condition, String command) {
    var cond = condition.split("\\|");
    if ("LVL".equals(cond[0])) {
      if (Game.get().isLevelCompleted(cond[1])) {
        parseCommand(command);
        return true;
      }
    }
    return false;
  }

  private int getNextID() {
    int id = 1;
    while (objs.containsKey(id)) {
      id++;
    }
    return id;
  }
}
