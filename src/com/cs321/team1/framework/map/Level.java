package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.menu.menus.LevelMenu;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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
        objs.stream().filter(Runnable.class::isInstance).forEach(it -> {
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
    
    //TODO handle actual level generation
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
                new PassableTile(level, Location.atTile(i, j), Textures.FLOOR);
        new Player(level, Location.atTile(width / 2, height / 2));
        return level;
    }
}
