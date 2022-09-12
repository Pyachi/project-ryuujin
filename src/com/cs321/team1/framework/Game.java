package com.cs321.team1.framework;

import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.tiles.Crate;
import com.cs321.team1.framework.objects.tiles.GroundTile;
import com.cs321.team1.framework.objects.tiles.IntegerCrate;
import com.cs321.team1.framework.objects.tiles.WallTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Game extends JPanel implements Runnable {
    //Singleton class setup
    private static Game i;

    //Screen size constants
    public static final int baseTileSize = 16;
    public static final int scale = 4;
    public static final int screenCol = 16;
    public static final int screenRow = 12;
    public static final int tileSize = baseTileSize * scale;
    public static final int screenWidth = tileSize * screenCol;
    public static final int screenHeight = tileSize * screenRow;

    //Fields for interaction with Swing
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

        //ConcurrentModificationException is caused by Swing and does not break anything, and as such can be ignored.
        try {
            List<GameObject> updates = new ArrayList<>(objects);
            updates.sort(Comparator.comparingInt(GameObject::getZ));
            updates.forEach(it -> it.paint(graphics));
        } catch (ConcurrentModificationException ignored) {
        }

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
            long remainingTime = nextTime - System.nanoTime();
            try {
                Thread.sleep(remainingTime / 1000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //*********************************************************************************
    //Actual game content from here forth

    private final List<GameObject> objects = new ArrayList<>();

    private void start() {
        for (int i = 0; i < 16; i++) {
            addObject(new WallTile(i, 0, Textures.WALL_TILE));
            addObject(new WallTile(i, 1, Textures.WALL_TILE));
            addObject(new WallTile(i, 10, Textures.WALL_TILE));
            addObject(new WallTile(i, 11, Textures.WALL_TILE));
        }
        for (int j = 2; j < 10; j++) {
            addObject(new WallTile(0, j, Textures.WALL_TILE));
            addObject(new WallTile(1, j, Textures.WALL_TILE));
            addObject(new WallTile(14, j, Textures.WALL_TILE));
            addObject(new WallTile(15, j, Textures.WALL_TILE));
        }
        for (int i = 2; i < 14; i++)
            for (int j = 2; j < 10; j++)
                addObject(new GroundTile(i, j, Textures.FLOOR_TILE));
        for (int i = 5; i < 7; i++)
            for (int j = 5; j < 7; j++)
                addObject(new WallTile(i, j, Textures.WALL_TILE));
        addObject(new IntegerCrate(8, 8, 1));
        addObject(new IntegerCrate(6, 8, 2));
        addObject(new Player());
    }

    private void update() {
        List<GameObject> list = new ArrayList<>(objects);
        list.stream().filter(it -> it instanceof Runnable).map(it -> (Runnable) it).forEach(Runnable::run);
    }

    public static void addObject(GameObject obj) {
        obj.update();
        i.objects.add(obj);
    }

    public static void removeObject(GameObject obj) {
        obj.update();
        i.objects.remove(obj);
    }

    public static List<GameObject> getObjects() {
        return new ArrayList<>(i.objects);
    }
}
