package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.Tick;
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
import java.util.Set;

public class Level implements GameSegment {
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private final Map<Method, Set<GameObject>> ticks = new HashMap<>();
    private final Dimension size;
    private final boolean world;
    private final BufferedImage image;
    private final BufferedImage level;
    private final Graphics2D graphics;
    private final Graphics2D levelGraphics;

    public Level(Dimension size, boolean world) {
        this.size = size;
        this.world = world;
        var screenSize = Game.get().getScreenSize();
        image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        level = new BufferedImage(size.w() * getScale(),
                size.h() * getScale(),
                BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        levelGraphics = level.createGraphics();
    }

    public void addBarriers() {
        addObject(new PassableTile(Location.Tile(1, 1), size, new Texture("map/base", -1)));
        addObject(new UnpassableTile(Location.Tile(1, 0), Dimension.Tile(size.w(), 1)));
        addObject(new UnpassableTile(Location.Tile(1, size.h() + 1), Dimension.Tile(size.w(), 1)));
        addObject(new UnpassableTile(Location.Tile(0, 1), Dimension.Tile(1, size.h())));
        addObject(new UnpassableTile(Location.Tile(size.w() + 1, 1), Dimension.Tile(1, size.h())));
    }

    public void addObject(GameObject obj) {
        obj.setLevel(this);
        objs.add(obj);
        objsToUpdate.add(obj);
        Arrays.stream(obj.getClass().getMethods())
                .filter(it -> it.isAnnotationPresent(Tick.class))
                .forEach(method -> ticks.computeIfAbsent(method, it -> new HashSet<>()).add(obj));
    }

    public void removeObject(GameObject obj) {
        objs.remove(obj);
        objsToUpdate.remove(obj);
        ticks.values().forEach(it -> it.remove(obj));
    }

    public Set<GameObject> getObjects() {
        return new HashSet<>(objs);
    }

    @Override
    public void update() {
        ticks.values().stream().flatMap(Collection::stream).forEach(it -> {
            objsToUpdate.add(it);
            objsToUpdate.addAll(it.getCollisions());
        });
        var methods = new ArrayList<>(ticks.keySet().stream().toList());
        methods.sort(Comparator.comparingInt(it -> it.getAnnotation(Tick.class).priority()));
        methods.forEach(method -> new HashSet<>(ticks.get(method)).forEach(obj -> {
            try {
                method.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }));
        if (Controls.BACK.isPressed()) Game.pushSegment(new LevelMenu(this));

    }

    @Override
    public void onClose() {
    }

    public int getScale() {
        int scale = 20;
        var screenSize = Game.get().getScreenSize();
        while (scale * size.w() > screenSize.width || scale * size.h() > screenSize.height) scale--;
        return scale;
    }

    @Override
    public void render(Graphics2D g) {
        var list = new ArrayList<>(objsToUpdate.stream().filter(it -> it.getTexture() != null).toList());
        objsToUpdate.clear();
        list.sort(Comparator.comparingInt(it -> it.getTexture().priority));
        list.forEach(it -> it.paint(levelGraphics));
        graphics.drawImage(level,
                (image.getWidth() - level.getWidth()) / 2,
                (image.getHeight() - level.getHeight()) / 2,
                null);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public boolean isWorld() {
        return world;
    }

    @Override
    public String toString() {
        var start = "SET|" + size.toString() + "|" + world + "\n";
        StringBuilder builder = new StringBuilder();
        for (GameObject obj : objs)
            builder.append(obj.toString()).append("\n");
        return start + builder;
    }

    public static Level fromString(String lvl) {
        var lines = lvl.replaceAll("\r","").split("\n");
        var set = lines[0].split("\\|");
        var level = new Level(Dimension.fromString(set[1]), Boolean.parseBoolean(set[2]));
        for (int i = 1; i < lines.length; i++) {
            var obj = GameObject.fromString(lines[i]);
            System.out.println(lines[i]);
            if (obj != null) {
                System.out.println(obj);
                level.addObject(obj);
            }
        }
        return level;
    }

    public Dimension getSize() {
        return size;
    }
}
