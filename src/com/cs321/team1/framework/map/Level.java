package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.menu.menus.LevelMenu;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.Tick;
import com.cs321.team1.framework.objects.crates.*;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Level extends GameComponent {
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private final Map<Method, Set<GameObject>> ticks = new HashMap<>();
    private final int width;
    private final int height;
    private boolean refresh = true;
    private BufferedImage image;
    private BufferedImage level;
    private Graphics2D graphics;
    private Graphics2D levelGraphics;
    
    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        refresh();
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
    public void refresh() {
        refresh = true;
        var screenSize = Game.get().getScreenSize();
        image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        level = new BufferedImage(getWidth() * 16 * getScale(),
                                  getHeight() * 16 * getScale(),
                                  BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        levelGraphics = level.createGraphics();
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
        if (Controls.BACK.isPressed()) Game.get().pushSegment(new LevelMenu(this));
        
    }
    
    @Override
    public void onClose() {
    }
    
    public int getScale() {
        int scale = 20;
        var screenSize = Game.get().getScreenSize();
        while (scale * 16 * width > screenSize.width || scale * 16 * height > screenSize.height) scale--;
        return scale;
    }
    
    @Override
    public void render(Graphics2D g) {
        var list = new ArrayList<GameObject>();
        if (refresh) list.addAll(objs);
        else list.addAll(objsToUpdate);
        objsToUpdate.clear();
        list.sort(Comparator.comparingInt(it -> it.getTexture().getTexture().priority));
        list.forEach(it -> it.paint(levelGraphics));
        graphics.drawImage(level,
                           (image.getWidth() - level.getWidth()) / 2,
                           (image.getHeight() - level.getHeight()) / 2,
                           null);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    public static Level emptyLevel(int width, int height) {
        var level = new Level(width, height);
        level.addObject(new UnpassableTile(Location.atTile(1, 0), Textures.NULL.get(width, 1)));
        level.addObject(new UnpassableTile(Location.atTile(1, height + 1), Textures.NULL.get(width, 1)));
        level.addObject(new UnpassableTile(Location.atTile(0, 1), Textures.NULL.get(1, height)));
        level.addObject(new UnpassableTile(Location.atTile(width + 1, 1), Textures.NULL.get(1, height)));
        level.addObject(new PassableTile(Location.atTile(1, 1), Textures.BASE.get(width, height)));
        return level;
    }
    
    public static Level loadLevel(String name) {
        try {
            String path = "src/resources/levels/" + name + ".ryu";
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String[] format = scanner.nextLine().split(":");
            int width = Integer.parseInt(format[0]);
            int height = Integer.parseInt(format[1]);
            Level level = emptyLevel(width, height);
            while (scanner.hasNext()) {
                try {
                    String[] obj = scanner.nextLine().split(":");
                    int x = Integer.parseInt(obj[0]);
                    int y = Integer.parseInt(obj[1]);
                    if (x > width || x < 1 || y > height || y < 1) continue;
                    Location loc = Location.atTile(x, y);
                    switch (obj[2]) {
                        case "PLAYER" -> level.addObject(new Player(loc));
                        case "FLOOR" -> level.addObject(new PassableTile(loc, Textures.valueOf(obj[3]).get()));
                        case "WALL" -> level.addObject(new UnpassableTile(loc, Textures.valueOf(obj[3]).get()));
                        case "INT" -> level.addObject(new IntegerCrate(loc, Integer.parseInt(obj[3])));
                        case "NEG" -> level.addObject(new NegateCrate(loc));
                        case "MOD" -> level.addObject(new ModuloCrate(loc, Integer.parseInt(obj[3])));
                        case "MUL" -> level.addObject(new MultiplyCrate(loc, Integer.parseInt(obj[3])));
                        case "DIV" -> level.addObject(new DivideCrate(loc, Integer.parseInt(obj[3])));
                    }
                } catch (Exception e) {
                    return emptyLevel(5, 5);
                }
            }
            return level;
        } catch (Exception e) {
            return emptyLevel(5, 5);
        }
    }
}
