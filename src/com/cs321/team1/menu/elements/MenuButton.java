package com.cs321.team1.menu.elements;

import com.cs321.team1.assets.Controls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuButton extends MenuElement {
    private final Runnable run;
    
    public MenuButton(String text, Runnable run) {
        super(text);
        this.run = run;
    }
    
    @Override
    public void update() {
        if (Controls.SELECT.isPressed()) run.run();
    }
    
    @Override
    public BufferedImage render(Font font, boolean selected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(getText());
        int textHeight = fontMetrics.getHeight();
        var image = new BufferedImage(textWidth + fontMetrics.stringWidth("AA"),
                                      textHeight + fontMetrics.getHeight(),
                                      BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        if (selected) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setColor(Color.WHITE);
        }
        graphics.setFont(font);
        int x = (image.getWidth() - textWidth) / 2;
        int y = (image.getHeight() - textHeight) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        return image;
    }
}
