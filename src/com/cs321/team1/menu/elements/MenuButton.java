package com.cs321.team1.menu.elements;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

/**
 * Menu Element for a clickable button with a single action
 */
public class MenuButton extends MenuElement {
    private final Runnable run;
    private boolean disabled;
    
    /**
     * Creates a MenuButton with the given name and action
     *
     * @param text The display on the MenuButton
     * @param run  The action the button will perform when clicked
     */
    public MenuButton(String text, Runnable run) {
        super(text);
        this.run = run;
    }
    
    /**
     * Creates a MenuButton with the given constant-width name and action
     *
     * @param size  The size of the display
     * @param left  The text on the left side of the display
     * @param right The text on the right side of the display
     * @param run   The action the button will perform when clicked
     */
    public MenuButton(int size, String left, String right, Runnable run) {
        super(size, left, right);
        this.run = run;
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
                BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();
        if (timeSelected != -1) {
            graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
            graphics.fillRect(0,
                    0,
                    Math.min((image.getWidth() / 10 * timeSelected), image.getWidth()),
                    image.getHeight());
        }
        graphics.setColor(disabled ? Color.DARK_GRAY : Color.WHITE);
        graphics.setFont(font);
        int x = fontMetrics.stringWidth("AA");
        int y = (image.getHeight() - textHeight / 2) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), x, y);
        return image;
    }
    
    @Override
    public void update() {
        if (Controls.SELECT.isPressed()) {
            if (!disabled) run.run();
            else Sounds.ERROR.play();
        }
    }
    
    /**
     * Checks whether the button is disabled
     *
     * @return True if the button is disabled, false otherwise
     */
    public boolean isDisabled() {
        return disabled;
    }
    
    /**
     * Disables or enables the button
     *
     * @param disabled Whether the button should be disabled or enabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
