package com.cs321.team1.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

public class Keyboard {
    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    
    public static void init(JFrame window) {
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (!heldKeys.contains(e.getKeyCode())) pressedKeys.add(e.getKeyCode());
                heldKeys.add(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                heldKeys.remove(e.getKeyCode());
            }
        });
    }
    
    public static boolean isKeyPressed(int key) {
        return pressedKeys.remove(key);
    }
    
    public static boolean isKeyHeld(int key) {
        return heldKeys.contains(key);
    }
}
