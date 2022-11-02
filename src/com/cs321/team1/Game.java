package com.cs321.team1;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Resolutions;
import com.cs321.team1.assets.ResourceLoader;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game extends JPanel {
    //******************************************************************************************************************
    //Framework
    
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
        
        Font tempFont;
        try {
            tempFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.loadStream("resources/PressStart.ttf"));
        } catch (Exception e) {
            tempFont = new JLabel().getFont();
        }
        font = tempFont;
        
        loadOptions();
        updateScreen();
        
        Runtime.getRuntime().addShutdownHook(new Thread(Game::saveOptions));
        
        startGameLogic();
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
    
    //******************************************************************************************************************
    //Game Logic
    
    private Timer logic;
    private final List<GameSegment> segments = new ArrayList<>();
    
    public static <T extends GameSegment> T getHighestSegmentOfType(Class<T> clazz) {
        return i.segments.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }
    
    public static int getIndexOf(GameSegment seg) {
        return i.segments.indexOf(seg);
    }
    
    public static void pushSegment(GameSegment seg) {
        seg.start();
        i.segments.add(0, seg);
    }
    
    public static void popSegment() {
        i.segments.remove(0).finish();
        if (!i.segments.isEmpty()) i.segments.get(0).refresh();
    }
    
    public static void popSegmentsTo(GameSegment seg) {
        while (i.segments.get(0) != seg) i.segments.remove(0).finish();
        i.segments.get(0).refresh();
    }
    
    private void startGameLogic() {
        pushSegment(new LoadingScreen());
        logic = new Timer();
        logic.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { update(); }
        }, 0L, 20L);
    }
    
    private void update() {
        try {
            if (getHighestSegmentOfType(Controls.ControlChanger.class) == null) {
                if (Controls.FULLSCREEN.isPressed()) {
                    toggleFullscreen();
                    updateScreen();
                }
                if (Controls.DEBUG.isPressed()) setDebugMode(!debug);
            }
            segments.get(0).update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //******************************************************************************************************************
    //Rendering
    
    private boolean debug = false;
    
    private Timer rendering;
    private Timer fpsTimer;
    private int tick = 0;
    private int measuredFPS = 0;
    private int fps = 0;
    
    public static BufferedImage getBlankImage() {
        return new BufferedImage(getScreenSize().width, getScreenSize().height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public static int getFPS() {
        return i.fps;
    }
    
    public static void setFPS(int fps) {
        i.fps = fps;
        if (i.rendering != null) {
            i.rendering.cancel();
            i.rendering.purge();
        }
        i.startGameRendering();
    }
    
    private void startGameRendering() {
        rendering = new Timer();
        long period = fps == 0 ? 1000 / GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getScreenDevices()[i.fullscreenMonitor].getDisplayMode().getRefreshRate() : 1000 / fps;
        rendering.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { repaint(); }
        }, 0L, period);
    }
    
    private void setDebugMode(boolean flag) {
        debug = flag;
        if (debug) {
            fpsTimer = new Timer();
            fpsTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    measuredFPS = tick;
                    tick = 0;
                }
            }, 0L, 1000L);
        } else {
            fpsTimer.cancel();
            fpsTimer.purge();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!segments.isEmpty()) g.drawImage(segments.get(0).render(), 0, 0, null);
        if (debug) {
            tick++;
            var f = font.deriveFont(Game.getScreenSize().height / 20F);
            var metrics = g.getFontMetrics(f);
            g.setColor(new Color(0F, 0F, 0F, 0.5F));
            g.fillRect(Game.getScreenSize().width - metrics.stringWidth(measuredFPS + "") * 3,
                    0,
                    metrics.stringWidth(measuredFPS + "") * 3,
                    metrics.getHeight() * 3);
            g.setFont(f);
            g.setColor(Color.WHITE);
            g.drawString(measuredFPS + "",
                    Game.getScreenSize().width - metrics.stringWidth(measuredFPS + "") * 2,
                    metrics.getHeight() * 2);
        }
    }
    
    //******************************************************************************************************************
    //Persistence
    
    public static void saveGame() {
        try {
            var file = new FileWriter("ryuujin.sav");
            i.segments.forEach(seg -> {
                if (!(seg instanceof Level)) return;
                try {
                    file.write(seg.toString());
                } catch (Exception e) { e.printStackTrace(); }
            });
            file.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void loadGame() {
        try {
            var lvlStrings = Files.readString(new File("ryuujin.sav").toPath()).split("SET");
            var levels = Arrays.stream(Arrays.copyOfRange(lvlStrings, 1, lvlStrings.length)).map(it -> Level.fromString(
                    "SET" + it)).toList();
            for (int x = levels.size() - 1; x >= 0; x--)
                Game.pushSegment(levels.get(x));
        } catch (Exception e) {
            Sounds.ERROR.play();
        }
    }
    
    private static void saveOptions() {
        try (var file = new FileWriter("options.txt")) {
            file.write("fullscreen: " + i.fullscreen + "\n");
            file.write("screenWidth: " + i.windowedScreenSize.width + "\n");
            file.write("screenHeight: " + i.windowedScreenSize.height + "\n");
            file.write("monitor: " + i.fullscreenMonitor + "\n");
            file.write("sound: " + Sounds.getVolume() + "\n");
            file.write("music: " + Music.getVolume() + "\n");
            file.write("fps: " + i.fps);
        } catch (Exception e) { e.printStackTrace(); }
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
                    if (str.startsWith("fps: ")) setFPS(Integer.parseInt(str.split("fps: ")[1]));
                } catch (Exception e) { e.printStackTrace(); }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    //******************************************************************************************************************
    //Main
    
    public static void main(String[] args) {
        new Game();
    }
}
