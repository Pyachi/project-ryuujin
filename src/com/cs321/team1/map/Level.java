package com.cs321.team1.map;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.Conveyor;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Tick;
import com.cs321.team1.objects.Trigger;
import com.cs321.team1.objects.UnpassableTile;
import com.cs321.team1.objects.crates.DivideCrate;
import com.cs321.team1.objects.crates.IntegerCrate;
import com.cs321.team1.objects.crates.LockedDoor;
import com.cs321.team1.objects.crates.ModuloCrate;
import com.cs321.team1.objects.crates.MultiplyCrate;
import com.cs321.team1.objects.crates.NegateCrate;
import com.cs321.team1.objects.crates.UnpoweredBeacon;
import com.cs321.team1.objects.world.LevelObject;
import com.cs321.team1.objects.world.NavPath;
import com.cs321.team1.objects.world.Navigator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Class handling a single level and all objects and logic within
 */
public class Level implements GameSegment {
    /**
     * The internal name of the level, for loading and storing completion status
     */
    public final String name;
    /**
     * Size of the level as a 2D vector
     */
    public final Vec2 size;
    /**
     * Boolean stating whether level is considered a world map or not, for menu interactions
     */
    public final boolean isWorld;
    private final Map<Integer, GameObject> objs = new HashMap<>();
    private final Map<Method, Set<GameObject>> ticks = new HashMap<>();
    private final Map<String, List<String>> cmds = new HashMap<>();
    private BufferedImage level;
    private Music music;
    
    /**
     * Creates a level with the given characteristics
     *
     * @param size    The size of the level
     * @param name    The internal name of the level
     * @param isWorld Whether the level is a world map
     */
    public Level(Vec2 size, String name, boolean isWorld) {
        this.size = size;
        this.name = name;
        this.isWorld = isWorld;
        onScreenSizeChange();
        addBase();
    }
    
    /**
     * Takes a serialized level string and reconstructs the level from it
     *
     * @param lvl The serialized level string
     * @return A level with all the specified components in the string
     */
    public static Level fromString(String lvl) {
        var lines = Arrays.stream(lvl.replaceAll("\r", "").split("\n")).filter(it -> !it.startsWith("//")).toArray(
                String[]::new);
        var set = lines[0].split("\\|");
        var level = new Level(Vec2.fromString(set[1]), set[2], Boolean.parseBoolean(set[3]));
        for (int i = 1; i < lines.length; i++) level.parseCommand(lines[i]);
        return level;
    }
    
    /**
     * Loads a level from the level folder
     *
     * @param name The internal name of the level
     */
    public static void load(String name) {
        try {
            Game.get().pushSegment(Level.fromString(String.join("\n", new BufferedReader(new InputStreamReader(
                    ResourceLoader.loadStream("resources/levels/" + name + ".ryu"))).lines().toArray(String[]::new))));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Adds a GameObject with a specified ID to the level
     *
     * @param id  The ID to associate with the object
     * @param obj The GameObject being added
     */
    public void addObject(int id, GameObject obj) {
        if (id != -1) {
            obj.setId(id);
            if (objs.containsKey(id)) removeObject(objs.get(id));
        }
        addObject(obj);
    }
    
    /**
     * Adds a GameObject with a random ID to the level
     *
     * @param obj The GameObject being added
     */
    public void addObject(GameObject obj) {
        obj.setLevel(this);
        if (obj.getID() == 0) obj.setId(getNextID());
        objs.put(obj.getID(), obj);
        Arrays.stream(obj.getClass().getMethods())
                .filter(it -> it.isAnnotationPresent(Tick.class))
                .forEach(method -> ticks.computeIfAbsent(method, it -> new HashSet<>()).add(obj));
    }
    
    /**
     * Gets the render of the level without re-rendering it
     *
     * @return The level render
     */
    public BufferedImage getLevelImage() {
        return level;
    }
    
    /**
     * Gets a set of all objects in the level
     *
     * @return A new set of all in-level objects
     */
    public Set<GameObject> getObjects() {
        return new HashSet<>(objs.values());
    }
    
    /**
     * Gets the maximum integer scalar of the level that can fit on the screen
     *
     * @return The maximum scalar of the level that can fit on the screen
     */
    public int getScale() {
        int scale = 20;
        var screenSize = Game.get().getRenderingManager().getScreenSize();
        while (scale * size.x() > screenSize.x() || scale * size.y() > screenSize.y()) scale--;
        return scale;
    }
    
    @Override
    public void onScreenSizeChange() {
        level = new BufferedImage(size.x() * getScale(), size.y() * getScale(), BufferedImage.TYPE_INT_ARGB);
        render();
    }
    
    @Override
    public void refresh() {
        if (music != null) music.play();
    }
    
    @Override
    public BufferedImage render() {
        var list = new ArrayList<>(objs.values().stream().filter(it -> it.getTexture() != null).toList());
        list.sort(Comparator.comparingInt(it -> it.getTexture().priority));
        var graphics = level.createGraphics();
        list.forEach(it -> it.paint(graphics));
        var image = Game.get().getRenderingManager().createImage();
        image.createGraphics().drawImage(level,
                (image.getWidth() - level.getWidth()) / 2,
                (image.getHeight() - level.getHeight()) / 2,
                null);
        return image;
    }
    
    @Override
    public void start() {
        if (music != null) music.play();
    }
    
    @Override
    public void update() {
        objs.values().forEach(GameObject::incTick);
        var methods = new ArrayList<>(ticks.keySet().stream().toList());
        methods.sort(Comparator.comparingInt(it -> it.getAnnotation(Tick.class).priority()));
        methods.forEach(method -> new HashSet<>(ticks.get(method)).forEach(obj -> {
            try { method.invoke(obj); } catch (Exception e) { e.printStackTrace(); }
        }));
        if (Controls.BACK.isPressed()) Game.get().pushSegment(new LevelMenu());
        if (!isWorld && objs.values().stream().noneMatch(UnpoweredBeacon.class::isInstance)) Game.get().completeLevel(
                name);
        cmds.forEach((condition, commandList) -> new ArrayList<>(commandList).forEach(command -> {
            if (checkCMD(condition, command)) commandList.remove(command);
        }));
    }
    
    /**
     * Removes given GameObject from the level
     *
     * @param obj The GameObject being removed
     */
    public void removeObject(GameObject obj) {
        objs.remove(obj.getID());
        ticks.values().forEach(it -> it.remove(obj));
    }
    
    /**
     * Removes GameObject with associated ID from the level
     *
     * @param id ID of GameObject to remove
     */
    public void removeObject(int id) {
        var obj = objs.remove(id);
        ticks.values().forEach(it -> it.remove(obj));
    }
    
    @Override
    public String toString() {
        removeBase();
        var start = "SET|" + size.toString() + "|" + name + "|" + isWorld + "\n";
        StringBuilder builder = new StringBuilder();
        if (music != null) builder.append("MSC|").append(music.name()).append("\n");
        cmds.forEach((condition, commandList) -> commandList.forEach(command -> builder.append("CMD|")
                .append(condition)
                .append("->")
                .append(command)
                .append("\n")));
        for (GameObject obj : objs.values()) builder.append(obj.toString()).append("\n");
        return start + builder;
    }
    
    private void addBase() {
        addObject(1, new PassableTile(new Vec2(1, 1).toTile(), size, new Texture("map/base", -1)));
        addObject(2, new UnpassableTile(new Vec2(1, 0).toTile(), new Vec2(size.x(), 1).toTile()));
        addObject(3, new UnpassableTile(new Vec2(1, size.y() / 16 + 1).toTile(), new Vec2(size.x(), 1).toTile()));
        addObject(4, new UnpassableTile(new Vec2(0, 1).toTile(), new Vec2(1, size.y()).toTile()));
        addObject(5, new UnpassableTile(new Vec2(size.x() / 16 + 1, 1).toTile(), new Vec2(1, size.y()).toTile()));
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
        while (objs.containsKey(id)) id++;
        return id;
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
                            if (line[2].contains("/")) addObject(id,
                                    new PassableTile(loc, Texture.fromString(line[2])));
                            else if (line.length == 4) addObject(id,
                                    new PassableTile(loc, Vec2.fromString(line[2]), Texture.fromString(line[3])));
                            else addObject(id, new PassableTile(loc, Vec2.fromString(line[2])));
                        }
                        case "WAL" -> {
                            if (line[2].contains("/")) addObject(id,
                                    new UnpassableTile(loc, Texture.fromString(line[2])));
                            else if (line.length == 4) addObject(id,
                                    new UnpassableTile(loc, Vec2.fromString(line[2]), Texture.fromString(line[3])));
                            else addObject(id, new UnpassableTile(loc, Vec2.fromString(line[2])));
                        }
                        case "TGR" -> {
                            var command = cmd.split("->")[1];
                            line = cmd.split("->")[0].split("\\|");
                            if (line[2].contains("/")) addObject(id,
                                    new Trigger(loc, Texture.fromString(line[2]), command));
                            else if (line.length == 4) addObject(id,
                                    new Trigger(loc, Vec2.fromString(line[2]), Texture.fromString(line[3]), command));
                            else addObject(id, new Trigger(loc, Vec2.fromString(line[2]), command));
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void removeBase() {
        for (int i = 1; i <= 5; i++) removeObject(i);
    }
}
