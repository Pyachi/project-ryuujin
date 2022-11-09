package com.cs321.team1.menu.elements;

import com.cs321.team1.game.Game;

import java.awt.Font;
import java.awt.image.BufferedImage;

/**
 * A MenuElement for displaying text with no interactions
 * MenuTexts also cannot be selected
 */
public class MenuText extends MenuElement {
    /**
     * Creates a MenuText element with the given display
     *
     * @param text The text to display on the element
     */
    public MenuText(String text) {
        super(text);
    }
    
    @Override
    public int getWidth(Font font) {
        return new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).stringWidth(getText() + "AAAA");
    }
    
    @Override
    public BufferedImage render(Font font, int timeSelected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textHeight = getHeight(font);
        var image = new BufferedImage(Game.get().getRenderingManager().getScreenSize().x(),
                textHeight,
                BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        graphics.setFont(font);
        int x = fontMetrics.stringWidth("AA");
        int y = (image.getHeight() - textHeight / 2) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        return image;
    }
    
    @Override
    public void update() { }
}
