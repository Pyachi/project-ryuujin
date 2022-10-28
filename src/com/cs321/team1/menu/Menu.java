package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.menu.elements.MenuElement;
import com.cs321.team1.menu.elements.MenuText;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu implements GameSegment {
    protected final List<MenuElement> elements = new ArrayList<>();
    private int selected = 0;
    private int tick = 0;
    
    @Override
    public void update() {
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
            Game.popSegment();
            Sounds.DESELECT.play();
        }
    }
    
    @Override
    public void refresh() {
        tick = 0;
    }
    
    private List<MenuElement> getSelectableElements() {
        return elements.stream().filter(it -> !(it instanceof MenuText)).toList();
    }
    
    @Override
    public BufferedImage render() {
        
        
        
        
        
        var screenSize = Game.getScreenSize();
        var image = Game.getBlankImage();
        var graphics = image.createGraphics();
        graphics.setColor(new Color(0f, 0f, 0f, 0.8f));
        graphics.fillRect(0, 0, screenSize.width, screenSize.height);
        graphics.setColor(Color.WHITE);
        var font = Game.font().deriveFont(((float) getTextSize()));
        var renders = elements.stream().map(it -> it.render(font,
                it == getSelectableElements().get(selected) ? tick++ : -1)).toList();
        int renderHeight = renders.stream().mapToInt(BufferedImage::getHeight).sum();
        int y = (screenSize.height - renderHeight) / 2;
        for (var render : renders) {
            graphics.drawImage(render, 0, y, null);
            y += render.getHeight();
        }
        return image;
    }
    
    private int getTextSize() {
        int maxWidth;
        int totalHeight;
        int textSize = Game.getScreenSize().height / 20 + 1;
        do {
            maxWidth = 0;
            totalHeight = 0;
            textSize--;
            for (MenuElement element : elements) {
                Font font = Game.font().deriveFont(((float) textSize));
                int width = element.getWidth(font);
                if (maxWidth < width) maxWidth = width;
                totalHeight += element.getHeight(font);
            }
        } while (maxWidth > Game.getScreenSize().width || totalHeight > Game.getScreenSize().height);
        return textSize;
    }
}
