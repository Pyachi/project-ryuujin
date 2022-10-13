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
    
    protected Level(int width, int height, boolean world) {
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
    
    public static void loadLevel(String name, boolean world) {
        try {
            var file = new Scanner(new File("src/resources/levels/" + name + ".ryu"));
            var setup = file.nextLine().split("\t");
            var mapSize = setup[1].split(":");
            var mapWidth = Integer.parseInt(mapSize[0]);
            var mapHeight = Integer.parseInt(mapSize[1]);
            var level = new Level(mapWidth, mapHeight, world);
            for (int i = 2; i < setup.length; i++) {
                var playerLoc = setup[i].split(":");
                var playerX = Integer.parseInt(playerLoc[0]);
                var playerY = Integer.parseInt(playerLoc[1]);
                level.addObject(new Player(Location.Tile(playerX, playerY)));
            }
            while (file.hasNext()) {
                var line = file.nextLine().split("\t");
                var locString = line[1].split(":");
                var locX = Integer.parseInt(locString[0]);
                var locY = Integer.parseInt(locString[1]);
                var loc = Location.Tile(locX, locY);
                switch (line[0]) {
                    case "FLR" -> {
                        if (line[2].contains("/")) {
                            var textureData = line[2].split(":");
                            var priority = Integer.parseInt(textureData[0]);
                            var path = textureData[1];
                            var texture = new Texture(path, priority);
                            level.addObject(new PassableTile(loc, texture));
                        } else {
                            var size = line[2].split(":");
                            var width = Integer.parseInt(size[0]);
                            var height = Integer.parseInt(size[1]);
                            if (line.length == 4) {
                                var textureData = line[3].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new PassableTile(loc, texture, width, height));
                            } else level.addObject(new PassableTile(loc, width, height));
                        }
                    }
                    case "WAL" -> {
                        if (line[2].contains("/")) {
                            var textureData = line[2].split(":");
                            var priority = Integer.parseInt(textureData[0]);
                            var path = textureData[1];
                            var texture = new Texture(path, priority);
                            level.addObject(new UnpassableTile(loc, texture));
                        } else {
                            var size = line[2].split(":");
                            var width = Integer.parseInt(size[0]);
                            var height = Integer.parseInt(size[1]);
                            if (line.length == 4) {
                                var textureData = line[3].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new UnpassableTile(loc, texture, width, height));
                            } else level.addObject(new UnpassableTile(loc, width, height));
                        }
                    }
                    case "INT" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new IntegerCrate(loc, val));
                    }
                    case "NEG" -> {
                        level.addObject(new NegateCrate(loc));
                    }
                    case "MOD" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new ModuloCrate(loc, val));
                    }
                    case "MUL" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new MultiplyCrate(loc, val));
                    }
                    case "DIV" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new DivideCrate(loc, val));
                    }
                    case "LCK" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new LockedCrate(loc, val));
                    }
                    case "PWR" -> {
                        var val = Integer.parseInt(line[2]);
                        level.addObject(new UnpoweredCrate(loc, val));
                    }
                    case "TGR" -> {
                        var command = file.nextLine().split("\t");
                        Runnable run = () -> {};
                        switch (command[0]) {
                            case "LVL" -> run = () -> {
                                if (Controls.SELECT.isPressed()) Level.loadLevel(command[1], false);
                            };
                        }
                        if (line[2].contains("/")) {
                            var textureData = line[2].split(":");
                            var priority = Integer.parseInt(textureData[0]);
                            var path = textureData[1];
                            var texture = new Texture(path, priority);
                            level.addObject(new Trigger(loc, texture, run));
                        } else {
                            var size = line[2].split(":");
                            var width = Integer.parseInt(size[0]);
                            var height = Integer.parseInt(size[1]);
                            if (line.length == 4) {
                                var textureData = line[3].split(":");
                                var priority = Integer.parseInt(textureData[0]);
                                var path = textureData[1];
                                var texture = new Texture(path, priority);
                                level.addObject(new Trigger(loc, texture, width, height, run));
                            } else level.addObject(new Trigger(loc, width, height, run));
                        }
                    }
                }
            }
            Game.pushSegment(level);
        } catch (Exception ignored) {
            System.out.println("Error could not load " + name);
            ignored.printStackTrace();
        }
    }
    
    public boolean isWorld() {
        return world;
    }
}
