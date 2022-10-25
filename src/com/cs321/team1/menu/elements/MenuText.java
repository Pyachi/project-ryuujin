package com.cs321.team1.menu.elements;

import com.cs321.team1.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class MenuText extends MenuElement {
    public MenuText(String text) {
        super(text);
    }
    
    @Override
    public void update() {}
    
    @Override
    public BufferedImage render(Font font, int timeSelected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textHeight = getHeight(font);
        var image = new BufferedImage(Game.getScreenSize().width, textHeight, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        graphics.setFont(font);
        int x = fontMetrics.stringWidth("AA");
        int y = (image.getHeight() - textHeight / 2) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        return image;
    }
    
    @Override
    public int getWidth(Font font) {
        return new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).stringWidth(getText() + "AAAA");
    }
}
