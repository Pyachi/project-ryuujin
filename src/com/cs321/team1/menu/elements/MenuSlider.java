package com.cs321.team1.menu.elements;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Controls;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class MenuSlider extends MenuElement {
    private int selected;
    private final int max;
    private final boolean showSlider;
    private final Consumer<Integer> run;
    
    public MenuSlider(String text, int selected, int max, boolean showSlider, Consumer<Integer> run) {
        super(text);
        this.selected = selected;
        this.max = max;
        this.showSlider = showSlider;
        this.run = run;
    }
    
    @Override
    public void update() {
        if (Controls.LEFT.isPressed()) {
            selected = Math.floorMod(selected - 1, max + 1);
            run.accept(selected);
        } else if (Controls.RIGHT.isPressed() || Controls.SELECT.isPressed()) {
            selected = (selected + 1) % (max + 1);
            run.accept(selected);
        }
    }
    
    @Override
    public BufferedImage render(Font font, int timeSelected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(getText());
        int textHeight = fontMetrics.getHeight();
        var image = new BufferedImage(Game.getScreenSize().width, textHeight * 2, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        if (selected != -1) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0, 0, Math.min((image.getWidth()/10*timeSelected), image.getWidth()), image.getHeight());
            graphics.setColor(Color.WHITE);
        }
        graphics.setFont(font);
        int x = fontMetrics.stringWidth("AA");
        int y = (image.getHeight() - textHeight) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        if (showSlider) {
            int offset = getWidth(font) / 2;
            graphics.fillRect(offset + x, y - textHeight / 2 - textHeight / 10, textWidth, textHeight / 5);
            graphics.fillRect(offset + x + (textWidth / max) * this.selected,
                    y - textHeight,
                    textHeight / 5,
                    textHeight);
        }
        return image;
    }
    
    @Override
    public int getWidth(Font font) {
        var stringWidth = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).stringWidth(
                getText() + "AAAA");
        return showSlider ? stringWidth * 2 : stringWidth;
    }
}
