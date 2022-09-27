package com.cs321.team1.framework;

import java.awt.Graphics2D;

public abstract class GameComponent {
    public abstract void update();
    
    public abstract void render(Graphics2D g);
    
    public abstract void refresh();
    
    public abstract void onClose();
    
    public int getIndex() {
        return Game.get().getSegments().indexOf(this);
    }
}
