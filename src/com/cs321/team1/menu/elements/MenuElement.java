package com.cs321.team1.menu.elements;

import java.awt.Font;
import java.awt.image.BufferedImage;

public abstract class MenuElement {
    private String text;
    
    protected MenuElement(String text) {
        this.text = text;
    }
    
    public abstract void update();
    
    public abstract BufferedImage render(Font font, int ticksSelected);
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setText(int size, String left, String right) {
        this.text = left + String.format("%" + (size - left.length()) + "s", right);
    }
    
    public int getHeight(Font font) {
        return new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).getHeight() * 2;
    }
    
    public abstract int getWidth(Font font);
}
