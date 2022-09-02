package com.cs321.team1.framework;

import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.PassableTile;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.UnpassableTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Game extends JPanel implements Runnable {
    public static Game i;

    public static final int baseTileSize = 16;
    public static final int scale = 4;
    public static final int screenCol = 16;
    public static final int screenRow = 12;
    public static final int tileSize = baseTileSize * scale;
    public static final int screenWidth = tileSize * screenCol;
    public static final int screenHeight = tileSize * screenRow;

    private final Thread gameThread = new Thread(this);
    private final BufferedImage screen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
    private final Graphics2D graphics = screen.createGraphics();

    private final List<GameObject> objects = new ArrayList<>();

    public Game() {
        i = this;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    public void start() {
        gameThread.start();
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    @Override
    public void run() {

        generateRoom();
        addObject(new Player());


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

    public void generateRoom() {
        for (int i = 0; i < 16; i++) {
            addObject(new UnpassableTile(i, 0, Textures.WALL_TILE));
            addObject(new UnpassableTile(i, 1, Textures.WALL_TILE));
            addObject(new UnpassableTile(i, 10, Textures.WALL_TILE));
            addObject(new UnpassableTile(i, 11, Textures.WALL_TILE));
        }
        for (int j = 2; j < 10; j++) {
            addObject(new UnpassableTile(0, j, Textures.WALL_TILE));
            addObject(new UnpassableTile(1, j, Textures.WALL_TILE));
            addObject(new UnpassableTile(14, j, Textures.WALL_TILE));
            addObject(new UnpassableTile(15, j, Textures.WALL_TILE));
        }
        for (int i = 2; i < 14; i++)
            for (int j = 2; j < 10; j++)
                addObject(new PassableTile(i, j, Textures.FLOOR_TILE));
    }

    public void update() {
        objects.stream().filter(it -> it instanceof Tickable).map(it -> (Tickable) it).forEach(Tickable::tick);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            objects.forEach(it -> it.paint(graphics));
        } catch (ConcurrentModificationException ignored) {
        }

        g.drawImage(screen, 0, 0, null);
    }
}
