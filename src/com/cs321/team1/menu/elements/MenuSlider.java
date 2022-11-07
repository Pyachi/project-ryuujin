package com.cs321.team1.menu.elements;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * MenuElement for handling a discrete value slider
 */
public class MenuSlider extends MenuElement {
    private final int max;
    private final boolean showSlider;
    private final Consumer<Integer> run;
    private int selected;
    
    /**
     * Creates a MenuSlider with a normal display
     *
     * @param text       The text display on the slider
     * @param selected   The currently selected mark on the slider
     * @param max        The maximum selectable mark on the slider
     * @param showSlider Whether an actual slider is visible or not
     * @param run        The action taken when the slider is adjusted
     */
    public MenuSlider(String text, int selected, int max, boolean showSlider, Consumer<Integer> run) {
        super(text);
        this.selected = selected;
        this.max = max;
        this.showSlider = showSlider;
        this.run = run;
    }
    
    /**
     * Creates a MenuSlider with a fixed length display
     *
     * @param size       The fixed size of the display
     * @param left       The text on the left of the display
     * @param right      The text on the right of the display
     * @param selected   The currently selected mark on the slider
     * @param max        The maximum selectable mark on the slider
     * @param showSlider Whether an actual slider is visible or not
     * @param run        The action taken when the slider is adjusted
     */
    public MenuSlider(int size, String left, String right, int selected, int max, boolean showSlider,
                      Consumer<Integer> run) {
        super(size, left, right);
        this.selected = selected;
        this.max = max;
        this.showSlider = showSlider;
        this.run = run;
    }
    
    @Override
    public int getWidth(Font font) {
        var stringWidth = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font).stringWidth(
                getText() + "AAAA");
        return showSlider ? stringWidth * 2 : stringWidth;
    }
    
    @Override
    public BufferedImage render(Font font, int timeSelected) {
        var fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(getText());
        int textHeight = fontMetrics.getHeight();
        var image = new BufferedImage(Game.get().getRenderingManager().getScreenSize().x(),
                textHeight * 2,
                BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();
        if (selected != -1) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0,
                    0,
                    Math.min((image.getWidth() / 10 * timeSelected), image.getWidth()),
                    image.getHeight());
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
    public void update() {
        if (Controls.LEFT.isPressed()) {
            selected = Math.floorMod(selected - 1, max + 1);
            run.accept(selected);
        } else if (Controls.RIGHT.isPressed() || Controls.SELECT.isPressed()) {
            selected = (selected + 1) % (max + 1);
            run.accept(selected);
        }
    }
}
