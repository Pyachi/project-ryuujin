package com.cs321.team1.framework;

import com.cs321.team1.framework.objects.*;
import com.cs321.team1.framework.objects.tiles.MovableTile;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.objects.tiles.tags.Collision;
import com.cs321.team1.framework.objects.tiles.tags.Movement;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Game extends JPanel implements Runnable {
    //Singleton class setup
    public static Game i;

    //Global variables
    public static final int baseTileSize = 16;
    public static final int scale = 4;
    public static final int screenCol = 16;
    public static final int screenRow = 12;
    public static final int tileSize = baseTileSize * scale;
    public static final int screenWidth = tileSize * screenCol;
    public static final int screenHeight = tileSize * screenRow;

    //Fields for interaction with Swing
    private final BufferedImage screen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
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
        for (int i = 5; i < 7; i++)
            for (int j = 5; j < 7; j++)
                addObject(new UnpassableTile(i, j, Textures.WALL_TILE));
        addObject(new MovableTile(8, 8, Textures.MOVABLE_TILE));
        addObject(new Player());
    }

    private void update() {
        getObjectsOfType(Collision.class).forEach(it -> {
            if (it instanceof GameObject) ((GameObject) it).getCollisions().forEach(GameObject::update);
        });
        getObjectsOfType(Movement.class).forEach(Movement::calculateMovement);
        getObjectsOfType(Collision.class).forEach(Collision::calculateCollision);
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public <T> List<T> getObjectsOfType(Class<T> clazz) {
        return objects.stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    public Player getPlayer() {
        return (Player) objects.stream().filter(it -> it instanceof Player).findFirst().orElseThrow();
    }
}
