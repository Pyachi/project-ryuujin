package com.cs321.team1;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements Runnable {
    //******************************************************************************************************************
    //Game Content
    
    private final List<GameComponent> segments = new ArrayList<>();
    
    private void start() {
        pushSegment(new MainMenu());
    }
    
    private void update() {
        Controls.cache();
        if (Controls.FULLSCREEN.isPressed()) toggleFullscreen();
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
    private Dimension windowedSize = new Dimension(640, 480);
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
        Controls.init(window);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        Font temp;
        try {
            temp = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart.ttf"));
        } catch (FontFormatException | IOException e) {
            temp = new JLabel().getFont();
        }
        font = temp;
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
            setScreenSize(new Dimension(window.getGraphicsConfiguration().getDevice().getDisplayMode().getWidth(),
                                        window.getGraphicsConfiguration().getDevice().getDisplayMode().getHeight()));
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
        if (!segments.isEmpty()) segments.get(0).render(((Graphics2D) g));
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
