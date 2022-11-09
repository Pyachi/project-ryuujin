package com.cs321.team1.assets;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.menu.ControlsMenu;
import com.cs321.team1.menu.Menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

/**
 * Enum class for in-game controls and utility commands for interacting with and changing them
 */
public enum Controls {
    /**
     * Moves the menu selector, level navigator, and player upwards on the screen. Default: W
     */
    UP(KeyEvent.VK_W),
    /**
     * Moves the menu selector, level navigator, and player downwards on the screen. Default: S
     */
    DOWN(KeyEvent.VK_S),
    /**
     * Moves the menu selector, level navigator, and player leftwards on the screen, Default: A
     */
    LEFT(KeyEvent.VK_A),
    /**
     * Moves the menu selector, level navigator, and player rightwards on the screen, Default: D
     */
    RIGHT(KeyEvent.VK_D),
    /**
     * Grabs crates as the player to move them around, Default: SHIFT
     */
    GRAB(KeyEvent.VK_SHIFT),
    /**
     * Selects menu elements and levels, and allows interactions with objects as the player, Default: SPACE
     */
    SELECT(KeyEvent.VK_SPACE),
    /**
     * Pauses the game, or returns out of a menu, Default: ESC
     */
    BACK(KeyEvent.VK_ESCAPE),
    /**
     * Toggles fullscreen on and off, Default: F11
     */
    FULLSCREEN(KeyEvent.VK_F11),
    /**
     * Toggles FPS counter on and off, Default: F3
     */
    DEBUG(KeyEvent.VK_F3);
    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static boolean initialized = false;
    private int key;
    
    Controls(int key) {
        this.key = key;
    }
    
    /**
     * Sets up the environment for receiving and operating on keyboard inputs
     * Does nothing if invoked more than once
     *
     * @param window The game window
     */
    public static void init(JFrame window) {
        if (initialized) return;
        initialized = true;
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                heldKeys.add(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                heldKeys.remove(e.getKeyCode());
            }
        });
    }
    
    /**
     * Gets the name of the given key-map from the associated integer key
     *
     * @param key The integer associated with the chosen key-map
     * @return The name of the key, or "UNBOUND" if -1
     */
    public static String keyNameFromInt(int key) { return key != -1 ? KeyEvent.getKeyText(key) : "UNBOUND"; }
    
    /**
     * Returns the integer key-map for the given action
     *
     * @return The integer key-map of the given action
     */
    public int getKey() {
        return key;
    }
    
    /**
     * Sets the key-map for the given action
     *
     * @param key The integer associated with the chosen key-map
     */
    public void setKey(int key) {
        this.key = key;
        pressedKeys.clear();
        heldKeys.clear();
    }
    
    /**
     * Checks if a given action key is held
     *
     * @return True if key is pressed for more than 1 tick, false otherwise
     */
    public boolean isHeld() { return heldKeys.contains(key); }
    
    /**
     * Checks if a given action key is pressed
     *
     * @return True if key is first pressed on that tick, false otherwise
     */
    public boolean isPressed() { return pressedKeys.remove(key); }
    
    /**
     * Class in charge of changing controls
     */
    public static class ControlChanger implements GameSegment {
        private final Controls control;
        private final ControlsMenu menu;
        private int tick = 0;
        
        /**
         * Creates a control changer for a given menu and action
         *
         * @param control The action to change the key-map of
         * @param menu    The underlying controls menu
         */
        public ControlChanger(Controls control, ControlsMenu menu) {
            this.control = control;
            this.menu = menu;
        }
        
        @Override
        public BufferedImage render() {
            return Game.get().getHighestSegmentOfType(Menu.class).render();
        }
        
        @Override
        public void update() {
            tick++;
            if (tick % 60 < 30) menu.getButton(control).setText(20, control.name() + ":", "_");
            else menu.getButton(control).setText(20, control.name() + ":", " ");
            if (pressedKeys.isEmpty()) return;
            int key = pressedKeys.stream().findAny().orElse(control.getKey());
            for (Controls other : Controls.values())
                if (menu.getNewKey(other) == key && other != control) menu.setNewKey(other, -1);
            menu.setNewKey(control, key);
            pressedKeys.clear();
            heldKeys.clear();
            Game.get().removeSegment(this);
        }
    }
}
