package com.cs321.team1.assets;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

/** Utility class for checking button presses and handling key-binds */
public enum Controls {
    /** Action for moving player upwards and moving up in menus */
    UP(KeyEvent.VK_W),
    /** Action for moving player downwards and moving down in menus */
    DOWN(KeyEvent.VK_S),
    /** Action for moving player leftwards and decrementing menu sliders */
    LEFT(KeyEvent.VK_A),
    /** Action for moving player rightwards and incrementing menu sliders */
    RIGHT(KeyEvent.VK_D),
    /** Action for grabbing crates as the player */
    GRAB(KeyEvent.VK_SHIFT),
    /** Action for selecting menu options and interacting as the player */
    SELECT(KeyEvent.VK_SPACE),
    /** Action for toggling fullscreen */
    FULLSCREEN(KeyEvent.VK_F11),
    /** Action for pausing the game and returning from menus */
    BACK(KeyEvent.VK_ESCAPE);

    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static final Set<Integer> keys = new HashSet<>();

    private int key;

    Controls(int key) {
        this.key = key;
    }

    /**
     * Gets the key value of the action.
     *
     * @return integer value of chosen key.
     */
    public int getKey() {
        return key;
    }

    /**
     * Sets the action to the chosen key value.
     *
     * @param key key value from {@link KeyEvent}
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Checks if given action is pressed.
     * All actions performed are considered pressed for one tick, even if held for longer.
     *
     * @return True if action is pressed, false if not.
     */
    public boolean isPressed() {
        return pressedKeys.remove(key);
    }

    /**
     * Checks if given action is held.
     *
     * @return True if action is held, false if not.
     */
    public boolean isHeld() {
        return heldKeys.contains(key);
    }

    public static void cache() {
        pressedKeys.clear();
        pressedKeys.addAll(keys);
        pressedKeys.removeAll(heldKeys);
        heldKeys.clear();
        heldKeys.addAll(keys);
    }

    public static void clearCache() {
        pressedKeys.clear();
        heldKeys.clear();
        keys.clear();
    }

    public static void init(JFrame window) {
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
            }
        });
    }

    public static Set<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public static Set<Integer> getHeldKeys() {
        return heldKeys;
    }
}
