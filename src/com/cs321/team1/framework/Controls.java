package com.cs321.team1.framework;

import com.cs321.team1.util.Keyboard;

import java.awt.event.KeyEvent;

public enum Controls {
    UP(KeyEvent.VK_W),
    DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A),
    RIGHT(KeyEvent.VK_D),
    GRAB(KeyEvent.VK_SHIFT),
    SELECT(KeyEvent.VK_SPACE),
    BACK(KeyEvent.VK_ESCAPE);
    
    private int key;
    
    private Controls(int key) {
        this.key = key;
    }
    
    public int getKey() {
        return key;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
    
    public boolean isPressed() {
        return Keyboard.isKeyPressed(key);
    }
    
    public boolean isHeld() {
        return Keyboard.isKeyHeld(key);
    }
}
