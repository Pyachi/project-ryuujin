package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.GameObject;
import com.cs321.team1.GameSegment;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.Conveyor;
import com.cs321.team1.objects.Navigator;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Trigger;
import com.cs321.team1.objects.UnpassableTile;
import com.cs321.team1.objects.crates.DivideCrate;
import com.cs321.team1.objects.crates.IntegerCrate;
import com.cs321.team1.objects.crates.LockedCrate;
import com.cs321.team1.objects.crates.ModuloCrate;
import com.cs321.team1.objects.crates.MultiplyCrate;
import com.cs321.team1.objects.crates.NegateCrate;
import com.cs321.team1.objects.crates.UnpoweredCrate;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Level implements GameSegment {
    private final Map<Integer, GameObject> objs = new HashMap<>();
    private final Map<Method, Set<GameObject>> ticks = new HashMap<>();
    private final Vec2 size;
    private final boolean world;
    private BufferedImage level;
    private Music music;
    
    public Level(Vec2 size, boolean world) {
        this.size = size;
        this.world = world;
        onScreenSizeChange();
        addBase();
    }
    
    private void addBase() {
        addObject(1, new PassableTile(new Vec2(1, 1).toTile(), size, new Texture("map/base", -1)));
        addObject(2, new UnpassableTile(new Vec2(1, 0).toTile(), new Vec2(size.x(), 1).toTile()));
        addObject(3, new UnpassableTile(new Vec2(1, size.y() / 16 + 1).toTile(), new Vec2(size.x(), 1).toTile()));
        addObject(4, new UnpassableTile(new Vec2(0, 1).toTile(), new Vec2(1, size.y()).toTile()));
        addObject(5, new UnpassableTile(new Vec2(size.x() / 16 + 1, 1).toTile(), new Vec2(1, size.y()).toTile()));
    }
    
    private void removeBase() {
        for (int i = 1; i <= 5; i++) removeObject(i);
    }
    
    public void addObject(int id, GameObject obj) {
        obj.setId(id);
        addObject(obj);
    }
    
    @Override
    public void refresh() {
        if (music != null) music.play();
    }
    
    @Override
    public void start() {
        if (music != null) music.play();
    }
    
    public void addObject(GameObject obj) {
        obj.setLevel(this);
        if (obj.getID() == 0) obj.setId(getRandomID());
        objs.put(obj.getID(), obj);
        Arrays.stream(obj.getClass().getMethods())
                .filter(it -> it.isAnnotationPresent(Tick.class))
                .forEach(method -> ticks.computeIfAbsent(method, it -> new HashSet<>()).add(obj));
    }
    
    public void removeObject(int id) {
        var obj = objs.remove(id);
        ticks.values().forEach(it -> it.remove(obj));
    }
    
    public void removeObject(GameObject obj) {
        objs.remove(obj.getID());
        ticks.values().forEach(it -> it.remove(obj));
    }
    
    public Set<GameObject> getObjects() {
        return new HashSet<>(objs.values());
    }
    
    @Override
    public void update() {
        objs.values().forEach(GameObject::incTick);
        var methods = new ArrayList<>(ticks.keySet().stream().toList());
        methods.sort(Comparator.comparingInt(it -> it.getAnnotation(Tick.class).priority()));
        methods.forEach(method -> new HashSet<>(ticks.get(method)).forEach(obj -> {
            try {
                method.invoke(obj);
            } catch (Exception e) {e.printStackTrace();}
        }));
        if (Controls.BACK.isPressed()) Game.pushSegment(new LevelMenu());
        
    }
    
    public int getScale() {
        int scale = 20;
        var screenSize = Game.getScreenSize();
        while (scale * size.x() > screenSize.width || scale * size.y() > screenSize.height) scale--;
        return scale;
    }
    
    @Override
    public void render(Graphics2D g) {
        var list = new ArrayList<>(objs.values().stream().filter(it -> it.getTexture() != null).toList());
        list.sort(Comparator.comparingInt(it -> it.getTexture().priority));
        var graphics = level.createGraphics();
        list.forEach(it -> it.paint(graphics));
        var image = new BufferedImage(Game.getScreenSize().width,
                Game.getScreenSize().height,
                BufferedImage.TYPE_INT_ARGB);
        image.createGraphics().drawImage(level,
                (image.getWidth() - level.getWidth()) / 2,
                (image.getHeight() - level.getHeight()) / 2,
                null);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    @Override
    public void onScreenSizeChange() {
        level = new BufferedImage(size.x() * getScale(), size.y() * getScale(), BufferedImage.TYPE_INT_ARGB);
    }
    
    public boolean isWorld() {
        return world;
    }
    
    public Vec2 getSize() {
        return size;
    }
    
    private int getRandomID() {
        Random rand = new Random();
        int id = 0;
        while (id == 0 || objs.containsKey(id)) id = rand.nextInt();
        return id;
    }
    
    //******************************************************************************************************************
    //Serialization
    
    @Override
    public String toString() {
        removeBase();
        var start = "SET|" + size.toString() + "|" + world + "\n";
        StringBuilder builder = new StringBuilder();
        if (music != null) builder.append("MSC|").append(music.name()).append("\n");
        for (GameObject obj : objs.values()) builder.append(obj.toString()).append("\n");
        return start + builder;
    }
    
    public static void load(String name) {
        try {
            Game.pushSegment(Level.fromString(String.join("\n",
                    new BufferedReader(new InputStreamReader(ResourceLoader.loadStream(
                            "resources/levels/" + name + ".ryu"))).lines().toArray(String[]::new))));
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public static Level fromString(String lvl) {
        var lines = Arrays.stream(lvl.replaceAll("\r", "").split("\n")).filter(it -> !it.startsWith("//")).toArray(
                String[]::new);
        var set = lines[0].split("\\|");
        var level = new Level(Vec2.fromString(set[1]), Boolean.parseBoolean(set[2]));
        for (int i = 1; i < lines.length; i++) level.parseCommand(lines[i]);
        return level;
    }
    
    private void parseCommand(String cmd) {
        try {
            var line = cmd.split("\\|");
            switch (line[0]) {
                case "MSC" -> music = Music.valueOf(line[1]);
                default -> {
                    var loc = Vec2.fromString(line[1]);
                    switch (line[0]) {
                        case "NAV" -> addObject(new Navigator(loc));
                        case "PLR" -> addObject(new Player(loc));
                        case "INT" -> addObject(new IntegerCrate(loc, Integer.parseInt(line[2])));
                        case "NEG" -> addObject(new NegateCrate(loc));
                        case "MOD" -> addObject(new ModuloCrate(loc, Integer.parseInt(line[2])));
                        case "MUL" -> addObject(new MultiplyCrate(loc, Integer.parseInt(line[2])));
                        case "DIV" -> addObject(new DivideCrate(loc, Integer.parseInt(line[2])));
                        case "LCK" -> addObject(new LockedCrate(loc, Integer.parseInt(line[2])));
                        case "PWR" -> addObject(new UnpoweredCrate(loc, Integer.parseInt(line[2])));
                        case "CVR" -> addObject(switch (line[2]) {
                            default -> Conveyor.UP(loc);
                            case "D" -> Conveyor.DOWN(loc);
                            case "L" -> Conveyor.LEFT(loc);
                            case "R" -> Conveyor.RIGHT(loc);
                        });
                        case "FLR" -> {
                            if (line[2].contains("/")) addObject(new PassableTile(loc, Texture.fromString(line[2])));
                            else if (line.length == 4) addObject(new PassableTile(loc,
                                    Vec2.fromString(line[2]),
                                    Texture.fromString(line[3])));
                            else addObject(new PassableTile(loc, Vec2.fromString(line[2])));
                        }
                        case "WAL" -> {
                            if (line[2].contains("/")) addObject(new UnpassableTile(loc, Texture.fromString(line[2])));
                            else if (line.length == 4) addObject(new UnpassableTile(loc,
                                    Vec2.fromString(line[2]),
                                    Texture.fromString(line[3])));
                            else addObject(new UnpassableTile(loc, Vec2.fromString(line[2])));
                        }
                        case "TGR" -> {
                            var tgr = cmd.split("->")[1];
                            line = cmd.split("->")[0].split("\\|");
                            if (line[2].contains("/")) addObject(new Trigger(loc, Texture.fromString(line[2]), tgr));
                            else if (line.length == 4) addObject(new Trigger(loc,
                                    Vec2.fromString(line[2]),
                                    Texture.fromString(line[3]),
                                    tgr));
                            else addObject(new Trigger(loc, Vec2.fromString(line[2]), tgr));
                        }
                    }
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }
}
