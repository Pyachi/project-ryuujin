package com.cs321.team1.map;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.menu.LevelMenu;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Tick;
import com.cs321.team1.objects.crates.*;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.UnpassableTile;

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
    
    public static Level loadLevel(String name, boolean world) {
        try {
            String path = "src/resources/levels/" + name + ".ryu";
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String[] format = scanner.nextLine().split(":");
            int width = Integer.parseInt(format[0]);
            int height = Integer.parseInt(format[1]);
            Level level = new Level(width, height, world);
            while (scanner.hasNext()) {
                try {
                    String[] obj = scanner.nextLine().split(":");
                    int x = Integer.parseInt(obj[0]);
                    int y = Integer.parseInt(obj[1]);
                    if (x > width || x < 1 || y > height || y < 1) continue;
                    Location loc = Location.Tile(x, y);
                    switch (obj[2]) {
                        case "PLR" -> level.addObject(new Player(loc));
                        case "FLR" -> {
                            if (obj.length == 5) {
                                var tileWidth = Integer.parseInt(obj[3]);
                                var tileHeight = Integer.parseInt(obj[4]);
                                level.addObject(new PassableTile(loc, tileWidth, tileHeight));
                            } else level.addObject(new PassableTile(loc, Texture.Basic(obj[3], 0)));
                        }
                        case "WAL" -> {
                            if (obj.length == 5) {
                                var tileWidth = Integer.parseInt(obj[3]);
                                var tileHeight = Integer.parseInt(obj[4]);
                                level.addObject(new UnpassableTile(loc, tileWidth, tileHeight));
                            } else level.addObject(new UnpassableTile(loc, Texture.Basic(obj[3], 0)));
                        }
                        case "INT" -> level.addObject(new IntegerCrate(loc, Integer.parseInt(obj[3])));
                        case "NEG" -> level.addObject(new NegateCrate(loc));
                        case "MOD" -> level.addObject(new ModuloCrate(loc, Integer.parseInt(obj[3])));
                        case "MUL" -> level.addObject(new MultiplyCrate(loc, Integer.parseInt(obj[3])));
                        case "DIV" -> level.addObject(new DivideCrate(loc, Integer.parseInt(obj[3])));
                        case "LCK" -> level.addObject(new LockedCrate(loc, Integer.parseInt(obj[3])));
                        case "PWR" -> level.addObject(new UnpoweredCrate(loc, Integer.parseInt(obj[3])));
                    }
                } catch (Exception ignored) {
                }
            }
            return level;
        } catch (Exception e) {
            return new Level(5, 5, false);
        }
    }
    
    public boolean isWorld() {
        return world;
    }
}
