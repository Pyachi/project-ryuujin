package com.cs321.team1.framework.map;

import com.cs321.team1.framework.Game;
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
import java.util.List;
import java.util.Set;

public class Level {
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    private boolean refresh = true;
    private final int width;
    private final int height;
    private final BufferedImage image;
    private final Graphics2D graphics;
    
    private Level(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(getWidth()*16*getScale(),getHeight()*16*getScale(),BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
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
    
    public void refresh() {
        refresh = true;
    }
    
    public void update() {
        if (refresh) {
            objsToUpdate.addAll(objs);
            objs.forEach(it -> it.getTexture().reset());
        } else {
            objs.stream().filter(Runnable.class::isInstance).forEach(it -> {
                objsToUpdate.add(it);
                objsToUpdate.addAll(it.getCollisions());
            });
        }
        objs.stream().filter(Runnable.class::isInstance).forEach(it -> {
            if (!it.isDead()) ((Runnable) it).run();
        });
    }
    
    public int getScale() {
        int scale = 10;
        while (scale * 16 * width > Game.get().getScreenWidth() || scale * 16 * height > Game.get().getScreenHeight())
            scale--;
        return scale;
    }
    
    public BufferedImage getImage() {
        List<GameObject> list = new ArrayList<>(objsToUpdate);
        objsToUpdate.clear();
        list.sort(Comparator.comparingInt(it -> it.getTexture().getTexture().priority));
        list.forEach(it -> it.paint(graphics));
        return image;
    }
    
    public static Level emptyLevel(int width, int height) {
        Level level = new Level(width, height);
        for (int i = 0; i <= width + 1; i++) {
            new UnpassableTile(level, i, 0, Textures.NOTHING);
            new UnpassableTile(level, i, height + 1, Textures.NOTHING);
        }
        for (int j = 1; j <= height; j++) {
            new UnpassableTile(level, 0, j, Textures.NOTHING);
            new UnpassableTile(level, width + 1, j, Textures.NOTHING);
        }
        for (int i = 1; i <= width; i++)
            for (int j = 1; j <= height; j++)
                new PassableTile(level, i, j, Textures.FLOOR_TILE);
        new Player(level, Location.atTile(width / 2, height / 2));
        return level;
    }
}
