package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.menu.menus.LevelMenu;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.crates.*;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level extends GameComponent {
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private boolean refresh = true;
    private final int width;
    private final int height;
    private BufferedImage image;
    private BufferedImage level;
    private Graphics2D graphics;
    private Graphics2D levelGraphics;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        var screenSize = Game.get().getScreenSize();
        image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        level = new BufferedImage(getWidth() * 16 * getScale(),
                getHeight() * 16 * getScale(),
                BufferedImage.TYPE_INT_ARGB
        );
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
        objs.add(obj);
        objsToUpdate.add(obj);
    }

    public void removeObject(GameObject obj) {
        objs.remove(obj);
        objsToUpdate.remove(obj);
    }

    public Set<GameObject> getObjects() {
        return new HashSet<>(objs);
    }

    public Player getPlayer() {
        return objs.stream().filter(Player.class::isInstance).map(Player.class::cast).findFirst().orElseThrow();
    }

    @Override
    public void refresh() {
        refresh = true;
        var screenSize = Game.get().getScreenSize();
        image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        level = new BufferedImage(getWidth() * 16 * getScale(),
                getHeight() * 16 * getScale(),
                BufferedImage.TYPE_INT_ARGB
        );
        graphics = image.createGraphics();
        levelGraphics = level.createGraphics();
    }

    @Override
    public void update() {
        objs.stream().filter(Runnable.class::isInstance).forEach(it -> {
            objsToUpdate.add(it);
            objsToUpdate.addAll(it.getCollisions());
        });
        new ArrayList<>(objs).stream().filter(Runnable.class::isInstance).forEach(it -> {
            if (!it.isDead()) ((Runnable) it).run();
        });
        if (Controls.BACK.isPressed()) {
            Game.get().pushSegment(new LevelMenu(this));
        }
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
                null
        );
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public static Level emptyLevel(int width, int height) {
        var level = new Level(width, height);
        for (int i = 0; i <= width + 1; i++) {
            new UnpassableTile(level, Location.atTile(i, 0), Textures.NOTHING);
            new UnpassableTile(level, Location.atTile(i, height + 1), Textures.NOTHING);
        }
        for (int j = 1; j <= height; j++) {
            new UnpassableTile(level, Location.atTile(0, j), Textures.NOTHING);
            new UnpassableTile(level, Location.atTile(width + 1, j), Textures.NOTHING);
        }
        for (int i = 1; i <= width; i++)
            for (int j = 1; j <= height; j++)
                new PassableTile(level, Location.atTile(i, j), Textures.BASE);
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
                        case "PLAYER" -> new Player(level, loc);
                        case "FLOOR" -> new PassableTile(level, loc, Textures.valueOf(obj[3]));
                        case "WALL" -> new UnpassableTile(level,loc,Textures.valueOf(obj[3]));
                        case "INT" -> new IntegerCrate(level, loc, Integer.parseInt(obj[3]));
                        case "NEG" -> new NegateCrate(level, loc);
                        case "MOD" -> new ModuloCrate(level, loc, Integer.parseInt(obj[3]));
                        case "MUL" -> new MultiplyCrate(level, loc, Integer.parseInt(obj[3]));
                        case "DIV" -> new DivideCrate(level, loc, Integer.parseInt(obj[3]));
                    }
                } catch (Exception ignored) {
                }
            }
            return level;
        } catch (Exception e) {
            return emptyLevel(5, 5);
        }
    }
}
