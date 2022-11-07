package com.cs321.team1.menu.elements;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Abstract class defining any interact-able elements in a menu
 */
public abstract class MenuElement {
    private String text;
    
    protected MenuElement(String text) { this.text = text; }
    
    protected MenuElement(int size, String left, String right) { setText(size, left, right); }
    
    /**
     * Gets the pixel height of the element
     *
     * @param font The font being used for the display
     * @return The pixel height of the element
     */
    public int getHeight(Font font) {
        return new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).getHeight() * 2;
    }
    
    /**
     * Gets the text display of the element
     *
     * @return The text display of the element
     */
    public String getText() { return text; }
    
    /**
     * Sets the text of the element
     *
     * @param text The text to set the display to
     */
    public void setText(String text) { this.text = text; }
    
    /**
     * Abstract method to get the pixel width of the element
     *
     * @param font The font being used for the display
     * @return The pixel width of the element
     */
    public abstract int getWidth(Font font);
    
    /**
     * Abstract method to handle rendering of the element
     *
     * @param font          The font being used for the display
     * @param ticksSelected The duration the element has been selected for
     * @return A rendered image for displaying on the screen
     */
    public abstract Image render(Font font, int ticksSelected);
    
    /**
     * Sets the text of the element to a fixed-length string
     *
     * @param size  The size of the string
     * @param left  The text on the left of the string
     * @param right The text on the right of the string
     */
    public void setText(int size, String left, String right) {
        this.text = left + String.format("%" + (size - left.length()) + "s", right);
    }
    
    /**
     * Abstract method to handle what the elements do every tick
     */
    public abstract void update();
}
