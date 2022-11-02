package com.cs321.team1.assets;

import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.menu.ControlsMenu;
import com.cs321.team1.menu.Menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

public enum Controls {
    UP(KeyEvent.VK_W),
    DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A),
    RIGHT(KeyEvent.VK_D),
    GRAB(KeyEvent.VK_SHIFT),
    SELECT(KeyEvent.VK_SPACE),
    BACK(KeyEvent.VK_ESCAPE),
    FULLSCREEN(KeyEvent.VK_F11),
    DEBUG(KeyEvent.VK_F3);
    
    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static boolean initialized = false;
    
    private int key;
    
    Controls(int key) {
        this.key = key;
    }
    
    public int getKey() {
        return key;
    }
    
    public void setKey(int key) {
        this.key = key;
        pressedKeys.clear();
        heldKeys.clear();
    }
    
    public boolean isPressed() { return pressedKeys.remove(key); }
    
    public boolean isHeld() { return heldKeys.contains(key); }
    
    public static String keyNameFromInt(int key) { return key != -1 ? KeyEvent.getKeyText(key) : "UNBOUND"; }
    
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
    
    public static class ControlChanger implements GameSegment {
        private final Controls control;
        private final ControlsMenu menu;
        private int tick = 0;
        
        public ControlChanger(Controls control, ControlsMenu menu) {
            this.control = control;
            this.menu = menu;
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
            Game.popSegment();
        }
        
        @Override
        public BufferedImage render() {
            return Game.getHighestSegmentOfType(Menu.class).render();
        }
    }
}
