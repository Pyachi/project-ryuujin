package com.cs321.team1;

import java.awt.Graphics2D;

public interface GameSegment {
    void update();
    
    void render(Graphics2D g);
    
    default void onScreenSizeChange() {}
    
    default void onClose() {}
}
