package com.cs321.team1.framework;

import com.cs321.team1.framework.map.Dungeon;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game extends JPanel implements Runnable {
    //******************************************************************************************************************
    //Game Content
    
    public static final int tileSize = 16;
    public static final int scale = 4;
    
    private final Player player = new Player();
    private Dungeon dungeon;
    
    private void start() {
        dungeon = Dungeon.generateDungeon();
    }
    
    private void update() {
        player.run();
        dungeon.getActiveRoom().update();
        Set<GameObject> set = new HashSet<>(dungeon.getActiveRoom().objs);
        set.stream().filter(Runnable.class::isInstance).map(Runnable.class::cast).forEach(Runnable::run);
    }
    
    public static Player getPlayer() {
        return i.player;
    }
    
    public static Dungeon getDungeon() {
        return i.dungeon;
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
    public static final Font font;
    
    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
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
        if (dungeon != null) dungeon.getActiveRoom().paint(graphics);
        if (player != null) player.getTexture().paint(player.getLocation(), graphics);
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
            long remainingTime = Math.max(nextTime - System.nanoTime(), 0);
            try {
                Thread.sleep(remainingTime / 1000000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
