package com.cs321.team1.menu;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.menu.elements.MenuElement;
import com.cs321.team1.menu.elements.MenuText;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class defining basic menu functionality
 */
public abstract class Menu implements GameSegment {
    protected final List<MenuElement> elements = new ArrayList<>();
    private int selected = 0;
    private int tick = 0;
    
    @Override
    public void refresh() {
        tick = 0;
    }
    
    @Override
    public BufferedImage render() {
        var screenSize = Game.get().getRenderingManager().getScreenSize();
        var image = Game.get().getRenderingManager().createImage();
        var graphics = image.createGraphics();
        graphics.setColor(new Color(0f, 0f, 0f, 0.8f));
        graphics.fillRect(0, 0, screenSize.x(), screenSize.y());
        graphics.setColor(Color.WHITE);
        var font = Game.get().getRenderingManager().getFont().deriveFont(((float) getTextSize()));
        var renders = elements.stream().map(it -> it.render(font,
                it == getSelectableElements().get(selected) ? tick : -1)).toList();
        int renderHeight = renders.stream().mapToInt(it -> it.getHeight(null)).sum();
        int y = (screenSize.y() - renderHeight) / 2;
        for (var render : renders) {
            graphics.drawImage(render, 0, y, null);
            y += render.getHeight(null);
        }
        return image;
    }
    
    @Override
    public void update() {
        tick++;
        if (Controls.DOWN.isPressed()) {
            tick = 0;
            selected++;
        } else if (Controls.UP.isPressed()) {
            tick = 0;
            selected--;
        }
        if (selected < 0) selected = getSelectableElements().size() - 1;
        else if (selected >= getSelectableElements().size()) selected = 0;
        getSelectableElements().get(selected).update();
        if (!(this instanceof MainMenu) && Controls.BACK.isPressed()) {
            Game.get().removeSegment(this);
            Sounds.DESELECT.play();
        }
    }
    
    private List<MenuElement> getSelectableElements() {
        return elements.stream().filter(it -> !(it instanceof MenuText)).toList();
    }
    
    private int getTextSize() {
        int maxWidth;
        int totalHeight;
        int textSize = Game.get().getRenderingManager().getScreenSize().y() / 20 + 1;
        do {
            maxWidth = 0;
            totalHeight = 0;
            textSize--;
            for (MenuElement element : elements) {
                Font font = Game.get().getRenderingManager().getFont().deriveFont(((float) textSize));
                int width = element.getWidth(font);
                if (maxWidth < width) maxWidth = width;
                totalHeight += element.getHeight(font);
            }
        } while (maxWidth > Game.get().getRenderingManager().getScreenSize().x() ||
                totalHeight > Game.get().getRenderingManager().getScreenSize().y());
        return textSize;
    }
}
