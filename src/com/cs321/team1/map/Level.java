package com.cs321.team1.map;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.*;
import com.cs321.team1.objects.crates.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Level implements GameSegment {
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private final Map<Method, Set<GameObject>> ticks = new HashMap<>();
    private final int width;
    private final int height;
    private final boolean world;
    private final BufferedImage image;
    private final BufferedImage level;
    private final Graphics2D graphics;
    private final Graphics2D levelGraphics;
    
    public Level(int width, int height, boolean world) {
        this.width = width;
        this.height = height;
        this.world = world;
        var screenSize = Game.get().getScreenSize();
        image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        level = new BufferedImage(getWidth() * 16 * getScale(),
                                  getHeight() * 16 * getScale(),
                                  BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        levelGraphics = level.createGraphics();
        addObject(new PassableTile(Location.Tile(1, 1), new Texture("map/base", -1), width, height));
        addObject(new UnpassableTile(Location.Tile(1, 0), width, 1));
        addObject(new UnpassableTile(Location.Tile(1, height + 1), width, 1));
        addObject(new UnpassableTile(Location.Tile(0, 1), 1, height));
        addObject(new UnpassableTile(Location.Tile(width + 1, 1), 1, height));
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
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
    public void onClose() {}
    
    public int getScale() {
        int scale = 20;
        var screenSize = Game.get().getScreenSize();
        while (scale * 16 * width > screenSize.width || scale * 16 * height > screenSize.height) scale--;
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
}
