package com.cs321.team1;

import java.awt.Graphics2D;

public interface GameSegment {
    /**
     * Ran when segment is created
     */
    default void start() {}
    
    /**
     * Ran once per tick, game logic
     */
    default void update() {};
    
    /**
     * Ran when segment is re-activated
     */
    default void refresh() {}
    
    /**
     * Ran when segment is deleted
     */
    default void finish() {}
    
    /**
     * Ran when screen size is adjusted
     */
    default void onScreenSizeChange() {}
    
    /**
     * Ran once per tick, image rendering
     */
    void render(Graphics2D g);
}
