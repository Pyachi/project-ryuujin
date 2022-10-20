package com.cs321.team1;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.Resolutions;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelEntrance;
import com.cs321.team1.menu.MainMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game extends JPanel {
    //******************************************************************************************************************
    //Game Logic
    
    private final List<GameSegment> segments = new ArrayList<>();
    
    private void start() {
        pushSegment(new LoadingScreen());
    }
    
    private void update() {
        try {
            Controls.cache();
            if (Controls.FULLSCREEN.isPressed()) {
                toggleFullscreen();
                updateScreen();
            }
            segments.get(0).update();
        } catch (ConcurrentModificationException ignored) {
        }
    }
    
    public static void save() {
        try {
            var file = new FileWriter("ryuujin.sav");
            i.segments.forEach(seg -> {
                if (!(seg instanceof Level)) return;
                try {
                    file.write(seg.toString());
                } catch (IOException ignored) {
                }
            });
            file.close();
        } catch (IOException ignored) {
        }
    }
    
    public static void load() {
        try {
            var lvlStrings = Files.readString(new File("ryuujin.sav").toPath()).split("SET");
            var levels = Arrays.stream(Arrays.copyOfRange(lvlStrings, 1, lvlStrings.length)).map(it -> Level.fromString(
                    "SET" + it)).toList();
            for (int x = levels.size() - 1; x >= 0; x--)
                Game.pushSegment(levels.get(x));
            Game.pushSegment(new LevelEntrance(levels.get(0)));
        } catch (IOException e) {
            Sounds.ERROR.play();
        }
    }
    
    public static void pushSegment(GameSegment seg) {
        i.segments.add(0, seg);
    }
    
    public static void popSegment() {
        i.segments.remove(0).onClose();
    }
    
    public static void popSegmentsTo(int size) {
        while (i.segments.size() > size) popSegment();
    }
    //******************************************************************************************************************
    //Framework and Utilities
    
    private static Game i;
    private final JFrame window;
    private final Font font;
    
    private Dimension windowedScreenSize = Resolutions._640x480.size;
    private int fullscreenMonitor = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
            .toList()
            .indexOf(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
    private boolean fullscreen = true;
    
    private Game() {
        i = this;
        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Project 龍神");
        window.add(this);
        window.setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        Controls.init(window);
        
        Font temp;
        try {
            temp = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart.ttf"));
        } catch (FontFormatException | IOException e) {
            temp = new JLabel().getFont();
        }
        font = temp;
        
        loadOptions();
        updateScreen();
        
        Runtime.getRuntime().addShutdownHook(new Thread(Game::saveOptions));
        
        new Thread(Game::run).start();
    }
    
    public static Font font() {
        return i.font;
    }
    
    public static int getMonitor() {
        return i.fullscreenMonitor;
    }
    
    public static void setMonitor(int monitor) {
        var monitors = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (monitor > monitors.length - 1) return;
        i.fullscreenMonitor = monitor;
    }
    
    public static Dimension getScreenSize() {
        var bounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getScreenDevices()[i.fullscreenMonitor].getDefaultConfiguration().getBounds();
        return i.fullscreen ? new Dimension(bounds.width, bounds.height) : i.windowedScreenSize;
    }
    
    public static void setScreenSize(Dimension res) {
        i.windowedScreenSize = res;
    }
    
    public static boolean isFullscreen() {
        return i.fullscreen;
    }
    
    public static void setFullscreen(boolean fullscreen) {
        i.fullscreen = fullscreen;
    }
    
    public static void toggleFullscreen() {
        setFullscreen(!i.fullscreen);
    }
    
    public static void updateScreen() {
        i.window.dispose();
        i.window.setUndecorated(i.fullscreen);
        i.window.setVisible(true);
        var monitors = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        var bounds = monitors[i.fullscreenMonitor].getDefaultConfiguration().getBounds();
        if (i.fullscreen) {
            i.window.setLocation(bounds.x, bounds.y);
            i.setPreferredSize(new Dimension(bounds.width, bounds.height));
        } else {
            i.window.setLocation(bounds.x + (bounds.width - i.windowedScreenSize.width) / 2,
                    bounds.y + (bounds.height - i.windowedScreenSize.height) / 2);
            i.setPreferredSize(i.windowedScreenSize);
        }
        i.window.pack();
        i.segments.forEach(GameSegment::onScreenSizeChange);
    }
    
    private static void saveOptions() {
        try (var file = new FileWriter("options.txt")) {
            file.write("fullscreen: " + i.fullscreen + "\n");
            file.write("screenWidth: " + i.windowedScreenSize.width + "\n");
            file.write("screenHeight: " + i.windowedScreenSize.height + "\n");
            file.write("monitor: " + i.fullscreenMonitor + "\n");
            file.write("sound: " + Sounds.getVolume() + "\n");
            file.write("music: " + Music.getVolume() + "\n");
        } catch (IOException ignored) {}
    }
    
    private static void loadOptions() {
        try (var file = new BufferedReader(new FileReader("options.txt"))) {
            for (String str : file.lines().toList()) {
                try {
                    if (str.startsWith("fullscreen: "))
                        setFullscreen(Boolean.parseBoolean(str.split("fullscreen: ")[1]));
                    if (str.startsWith("screenWidth: ")) setScreenSize(new Dimension(Integer.parseInt(str.split(
                            "screenWidth: ")[1]), getScreenSize().height));
                    if (str.startsWith("screenHeight: ")) setScreenSize(new Dimension(getScreenSize().width,
                            Integer.parseInt(str.split("screenHeight: ")[1])));
                    if (str.startsWith("monitor: ")) setMonitor(Integer.parseInt(str.split("monitor: ")[1]));
                    if (str.startsWith("sound: ")) Sounds.setVolume(Integer.parseInt(str.split("sound: ")[1]));
                    if (str.startsWith("music: ")) Music.setVolume(Integer.parseInt(str.split("music: ")[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ignored) {}
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!segments.isEmpty()) segments.get(0).render(((Graphics2D) g));
    }
    
    @SuppressWarnings({"BusyWait"})
    private static void run() {
        i.start();
        while (true) {
            long interval = 1000000000 / 60;
            long nextTime = System.nanoTime() + interval;
            i.update();
            i.repaint();
            long remainingTime = Math.max(nextTime - System.nanoTime(), 0);
            try {
                Thread.sleep(remainingTime / 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        new Game();
    }
}
