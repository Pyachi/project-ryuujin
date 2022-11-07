package com.cs321.team1.game;

import java.awt.image.BufferedImage;

public interface GameSegment {
    /**
     * Ran when segment is deleted
     */
    default void finish() { }
    
    /**
     * Ran when screen size is adjusted
     */
    default void onScreenSizeChange() { }
    
    /**
     * Ran when segment is re-activated
     */
    default void refresh() { }
    
    /**
     * Ran once per tick, image rendering
     */
    BufferedImage render();
    
    /**
     * Ran when segment is created
     */
    default void start() { }
    
    /**
     * Ran once per tick, game logic
     */
    default void update() { }
}
