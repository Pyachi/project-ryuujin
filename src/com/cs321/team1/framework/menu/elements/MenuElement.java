package com.cs321.team1.framework.menu.elements;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class MenuElement {
    private String text;
    
    protected MenuElement(String text) {
        this.text = text;
    }
    
    public abstract void update();
    public abstract BufferedImage render(Font font, boolean selected);
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}
