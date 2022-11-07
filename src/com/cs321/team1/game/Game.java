package com.cs321.team1.game;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.LoadingScreen;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Top-of-hierarchy class containing everything game-related
 */
public class Game {
    private static Game instance = null;
    private final List<GameSegment> segments = new ArrayList<>();
    private final Set<String> completedLevels = new HashSet<>();
    private final RenderingManager renderingManager = new RenderingManager();
    
    private Game() {
        instance = this;
        renderingManager.loadOptions();
        renderingManager.updateScreen();
        startGameLogic();
    }
    
    /**
     * Gets the game instance
     *
     * @return The game instance
     */
    public static Game get() {
        if (instance == null) instance = new Game();
        return instance;
    }
    
    /**
     * Starts the program by creating the game instance
     *
     * @param args Program arguments (usually empty)
     */
    public static void main(String[] args) { Game.get(); }
    
    /**
     * Registers a level as completed
     *
     * @param lvl The name of the level
     */
    public void completeLevel(String lvl) { completedLevels.add(lvl); }
    
    /**
     * Returns the most recent segment of specified type
     *
     * @param clazz Class type of segment being searched for
     * @param <T>   Generic type encapsulating GameSegment subclass
     * @return Segment of chosen type if exists, null if none found
     */
    public <T extends GameSegment> T getHighestSegmentOfType(Class<T> clazz) {
        return segments.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }
    
    /**
     * Gets the RenderingManager
     *
     * @return The RenderingManager
     */
    public RenderingManager getRenderingManager() { return renderingManager; }
    
    /**
     * Checks if a level has been completed
     *
     * @param lvl The name of the level
     * @return True if level has been completed, false otherwise
     */
    public boolean isLevelCompleted(String lvl) { return completedLevels.contains(lvl); }
    
    /**
     * Removes a GameSegment from the top of the stack
     */
    public void popSegment() {
        segments.remove(0).finish();
        if (!segments.isEmpty()) segments.get(0).refresh();
    }
    
    /**
     * Removes GameSegments until the top of the stack is the specified type
     *
     * @param clazz Specified type to set to the top of the stack
     */
    public void popSegmentsTo(Class<? extends GameSegment> clazz) {
        while (!clazz.isInstance(segments.get(0))) segments.remove(0).finish();
        segments.get(0).refresh();
    }
    
    /**
     * Adds a GameSegment to the top of the stack
     *
     * @param seg The GameSegment to be added
     */
    public void pushSegment(GameSegment seg) {
        seg.start();
        segments.add(0, seg);
    }
    
    /**
     * Saves the game-state to a file
     */
    public void saveGame() {
        try {
            var file = new FileWriter("ryuujin.sav");
            completedLevels.forEach(lvl -> {
                try {
                    file.write("CMP|" + lvl + "\n");
                } catch (Exception e) { e.printStackTrace(); }
            });
            segments.forEach(seg -> {
                if (!(seg instanceof Level)) return;
                try {
                    file.write(seg.toString());
                } catch (Exception e) { e.printStackTrace(); }
            });
            file.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Gets list of all existing game segments
     *
     * @return New list of all game segments
     */
    List<GameSegment> getSegments() { return new ArrayList<>(segments); }
    
    private void startGameLogic() {
        pushSegment(new LoadingScreen());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { update(); }
        }, 0L, 20L);
    }
    
    private void update() {
        try {
            if (getHighestSegmentOfType(Controls.ControlChanger.class) == null) {
                if (Controls.FULLSCREEN.isPressed()) {
                    renderingManager.toggleFullscreen();
                    renderingManager.updateScreen();
                }
                if (Controls.DEBUG.isPressed()) renderingManager.toggleDebugMode();
            }
            segments.get(0).update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
