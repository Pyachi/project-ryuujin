package com.cs321.team1.framework;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public enum Controls {
    UP(KeyEvent.VK_W),
    DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A),
    RIGHT(KeyEvent.VK_D),
    GRAB(KeyEvent.VK_SHIFT),
    SELECT(KeyEvent.VK_SPACE),
    FULLSCREEN(KeyEvent.VK_F11),
    BACK(KeyEvent.VK_ESCAPE);
    
    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static final Set<Integer> keys = new HashSet<>();
    
    private int key;
    
    Controls(int key) {
        this.key = key;
    }
    
    public int getKey() {
        return key;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
    
    public boolean isPressed() {
        return pressedKeys.remove(key);
    }
    
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
