package com.cs321.team1;

import java.awt.*;

/**
 * Interface implementing a stackable game element
 */
public interface GameSegment {
    /**
     * Method in charge of handling game logic
     * Ran once every tick
     */
    void update();
    
    /**
     * Method in charge of handling rendering
     * Ran once every tick
     *
     * @param g Tool used to draw graphics
     */
    void render(Graphics2D g);
    
    /**
     * Method in charge of cleaning up object when scheduled for removal
     */
    void onClose();
}
