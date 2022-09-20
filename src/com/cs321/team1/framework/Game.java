package com.cs321.team1.framework;

import com.cs321.team1.framework.map.Level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {
    //******************************************************************************************************************
    //Game Content
    
    private Level level;
    
    private void start() {
        level = Level.emptyLevel(8, 9);
    }
    
    private void update() {
        level.update();
    }
    
    public Level getLevel() {
        return i.level;
    }
    //******************************************************************************************************************
    //Framework
    
    private static Game i;
    private int screenWidth = 1280;
    private int screenHeight = 720;
    private final Font font;
    
    public static Game get() {
        if (i == null) i = new Game();
        return i;
    }
    
    private Game() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(this).start();
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }
    
    public int getScreenHeight() {
        return screenHeight;
    }
    
    public Font getFont() {
        return font;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (level != null) {
            BufferedImage image = level.getImage();
            g.drawImage(image, (getScreenWidth() - image.getWidth())/2,(getScreenHeight() - image.getHeight())/2, null);
        }
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
