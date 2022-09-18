package com.cs321.team1.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class Keyboard {
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();
    
    public static void init(JFrame window) {
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                Keyboard.pressedKeys.put(e.getKeyCode(), true);
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                Keyboard.pressedKeys.put(e.getKeyCode(), false);
            }
        });
    }
    
    public static boolean isKeyPressed(int key) {
        return pressedKeys.getOrDefault(key, false);
    }
}
