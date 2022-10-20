package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.GameObject;
import com.cs321.team1.GameSegment;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.UnpassableTile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
            } catch (IllegalAccessException | InvocationTargetException ignored) {}
        }));
        if (Controls.BACK.isPressed()) Game.pushSegment(new LevelMenu(this));
        
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
        for (GameObject obj : objs.values()) builder.append(obj.toString()).append("\n");
        return start + builder;
    }
    
    public static Level fromString(String lvl) {
        var lines = Arrays.stream(lvl.replaceAll("\r", "").split("\n")).filter(it -> !it.startsWith("//")).toArray(
                String[]::new);
        var set = lines[0].split("\\|");
        var level = new Level(Vec2.fromString(set[1]), Boolean.parseBoolean(set[2]));
        for (int i = 1; i < lines.length; i++) {
            var obj = GameObject.fromString(lines[i]);
            if (obj != null) level.addObject(obj);
        }
        return level;
    }
}
