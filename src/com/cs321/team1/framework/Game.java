package com.cs321.team1.framework;

import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.MainMenu;
import com.cs321.team1.util.Keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game extends JPanel implements Runnable {
    //******************************************************************************************************************
    //Game Content
    
    private final List<GameComponent> segments = new ArrayList<>();
    
    private void start() {
        pushSegment(new MainMenu());
    }
    
    private void update() {
        segments.get(0).update();
    }
    
    public List<GameComponent> getSegments() {
        return new ArrayList<>(segments);
    }
    
    public void pushSegment(GameComponent seg) {
        segments.add(0, seg);
    }
    
    public void popSegment() {
        segments.remove(0).onClose();
    }
    //******************************************************************************************************************
    //Framework
    
    private static Game i;
    private final JFrame window;
    private final Font font;
    private Dimension windowedSize = Resolutions._640x480.size;
    private Dimension fullscreenSize;
    private boolean fullscreen = false;
    
    public static Game get() {
        if (i == null) i = new Game();
        return i;
    }
    
    private Game() {
        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Project 龍神");
        window.add(this);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        Keyboard.init(window);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        toggleFullscreen();
        updateScreen();
        new Thread(this).start();
    }
    
    public Font getFont() {
        return font;
    }
    
    public Dimension getScreenSize() {
        if (fullscreen) return fullscreenSize;
        else return windowedSize;
    }
    
    public void setScreenSize(Dimension size) {
        if (!fullscreen) windowedSize = size;
        else fullscreenSize = size;
        updateScreen();
    }
    
    public void toggleFullscreen() {
        if (!fullscreen) {
            fullscreen = true;
            window.dispose();
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
            window.setVisible(true);
            setScreenSize(new Dimension(
                    window.getGraphicsConfiguration().getDevice().getDisplayMode().getWidth(),
                    window.getGraphicsConfiguration().getDevice().getDisplayMode().getHeight()
            ));
        } else {
            fullscreen = false;
            window.dispose();
            window.setExtendedState(Frame.NORMAL);
            window.setUndecorated(false);
            window.setVisible(true);
            setScreenSize(windowedSize);
        }
    }
    
    public boolean isFullscreen() {
        return fullscreen;
    }
    
    public void updateScreen() {
        setPreferredSize(getScreenSize());
        window.pack();
        segments.forEach(GameComponent::refresh);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!segments.isEmpty()) g.drawImage(segments.get(0).render(), 0, 0, null);
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
