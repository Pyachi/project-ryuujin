package com.cs321.team1.menu.elements;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class MenuButton extends MenuElement {
    private final Runnable run;
    private boolean disabled;
    
    public MenuButton(String text, Runnable run) {
        super(text);
        this.run = run;
    }
    
    @Override
    public void update() {
        if (Controls.SELECT.isPressed()) {
            if (!disabled) run.run();
            else Sounds.ERROR.play();
        }
    }
    
    @Override
    public BufferedImage render(Font font, int timeSelected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textHeight = getHeight(font);
        var image = new BufferedImage(Game.getScreenSize().width, textHeight, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        if (timeSelected != -1) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0, 0, Math.min((image.getWidth()/10*timeSelected),image.getWidth()), image.getHeight());
        }
        graphics.setColor(disabled ? Color.DARK_GRAY : Color.WHITE);
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
    
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public boolean isDisabled() {
        return disabled;
    }
}
