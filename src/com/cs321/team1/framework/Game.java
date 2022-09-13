package com.cs321.team1.framework;

import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.crates.IntegerCrate;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.util.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Game extends JPanel implements Runnable {
    //******************************************************************************************************************
    //Game Content
    
    public static final int tileSize = 16;
    public static final int scale = 4;
    
    private final Set<GameObject> objs = new HashSet<>();
    private final Set<GameObject> objsToUpdate = new HashSet<>();
    
    private void start() {
        //TODO Implement random generation
        addObject(new PassableTile(0, 0, 0, Textures.BACKGROUND));
        for (int i = 1; i < 15; i++) {
            addObject(new UnpassableTile(i, 1, 0, Textures.NOTHING));
            addObject(new UnpassableTile(i, 10, 0, Textures.NOTHING));
        }
        for (int j = 2; j < 10; j++) {
            addObject(new UnpassableTile(1, j, 0, Textures.NOTHING));
            addObject(new UnpassableTile(14, j, 0, Textures.NOTHING));
        }
        for (int i = 2; i < 14; i++)
            for (int j = 2; j < 10; j++)
                addObject(new PassableTile(i, j, 0, Textures.FLOOR_TILE));
        for (int i = 5; i < 7; i++)
            for (int j = 5; j < 7; j++)
                addObject(new UnpassableTile(i, j, 1, Textures.WALL_TILE));
        addObject(new IntegerCrate(8, 8, 1));
        addObject(new IntegerCrate(6, 8, 2));
        addObject(new Player());
    }
    
    private void update() {
        List<GameObject> list = new ArrayList<>(objs);
        list.stream().filter(Runnable.class::isInstance).forEach(it -> {
            objsToUpdate.add(it);
            objsToUpdate.addAll(it.getCollisions());
        });
        list.stream().filter(Runnable.class::isInstance).map(Runnable.class::cast).forEach(Runnable::run);
        list.stream().filter(GameObject::isDead).forEach(it -> {
            objs.remove(it);
            objsToUpdate.remove(it);
        });
    }
    
    public static void addObject(GameObject obj) {
        i.objs.add(obj);
        i.objsToUpdate.add(obj);
    }
    
    public static List<GameObject> getObjects() {
        return new ArrayList<>(i.objs);
    }
    //******************************************************************************************************************
    //Framework
    
    private static Game i;
    
    private static final int screenCol = 16;
    private static final int screenRow = 12;
    private static final int screenTileSize = tileSize * scale;
    private static final int screenWidth = screenTileSize * screenCol;
    private static final int screenHeight = screenTileSize * screenRow;
    
    private final BufferedImage screen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    private final Graphics2D graphics = screen.createGraphics();
    
    public Game() {
        i = this;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        new Thread(this).start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<GameObject> list = new ArrayList<>(objsToUpdate);
        objsToUpdate.clear();
        list.sort(Comparator.comparingInt(it -> it.renderPriority));
        list.forEach(it -> it.paint(graphics));
        g.drawImage(screen, 0, 0, null);
    }
    
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    @Override
    public void run() {
        start();
        while (true) {
            long interval = 1000000000 / 60;
            long nextTime = System.nanoTime() + interval;
            update();
            repaint();
            long remainingTime = Math.max(nextTime - System.nanoTime(),0);
            try {
                Thread.sleep(remainingTime / 1000000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
