package com.cs321.team1.framework.menu.elements;

import com.cs321.team1.framework.Controls;
import com.cs321.team1.util.Keyboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class MenuSlider extends MenuElement {
    private int selected;
    private final int max;
    private final Consumer<Integer> run;
    
    public MenuSlider(String text, int selected, int max, Consumer<Integer> run) {
        super(text);
        this.selected = selected;
        this.max = max;
        this.run = run;
    }
    
    @Override
    public void update() {
        if (Controls.LEFT.isPressed()) {
            selected = Math.floorMod(selected - 1, max + 1);
            run.accept(selected);
        } else if (Controls.RIGHT.isPressed()) {
            selected = (selected + 1) % (max + 1);
            run.accept(selected);
        }
    }
    
    @Override
    public BufferedImage render(Font font, boolean selected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(getText());
        int textHeight = fontMetrics.getHeight();
        var image = new BufferedImage(
                textWidth + fontMetrics.stringWidth("AA"),
                textHeight + fontMetrics.getHeight() * 2,
                BufferedImage.TYPE_INT_ARGB
        );
        var graphics = image.createGraphics();
        if (selected) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setColor(Color.WHITE);
        }
        graphics.setFont(font);
        int x = (image.getWidth() - textWidth) / 2;
        int y = (image.getHeight() - textHeight) / 2 + fontMetrics.getAscent() - image.getHeight() / 4;
        int y2 = (image.getHeight() - textHeight) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        graphics.fillRect(x, y2 - textHeight / 10, textWidth, textHeight / 5);
        graphics.fillRect(x + (textWidth / max) * this.selected, y2 - textHeight / 2, textHeight / 5, textHeight);
        return image;
    }
}
