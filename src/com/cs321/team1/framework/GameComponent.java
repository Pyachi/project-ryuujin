package com.cs321.team1.framework;

import java.awt.image.BufferedImage;

public abstract class GameComponent {
    public abstract void update();
    public abstract BufferedImage render();
    public abstract void refresh();
    public abstract void onClose();
    
    public int getIndex() {
        return Game.get().getSegments().indexOf(this);
    }
}
