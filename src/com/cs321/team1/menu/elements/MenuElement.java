package com.cs321.team1.menu.elements;

import java.awt.*;
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
