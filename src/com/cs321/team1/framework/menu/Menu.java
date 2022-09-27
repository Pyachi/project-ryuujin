package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.framework.menu.elements.MenuElement;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.util.Keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu extends GameComponent {
    protected final List<MenuElement> elements = new ArrayList<>();
    private int selected = 0;
    
    @Override
    public void update() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) selected++;
        else if (Keyboard.isKeyPressed(KeyEvent.VK_W)) selected--;
        if (selected < 0) selected = elements.size() - 1;
        else if (selected >= elements.size()) selected = 0;
        elements.get(selected).update();
        if (Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE) && getIndex() != Game.get().getSegments().size() - 1) {
            Game.get().popSegment();
            Sounds.DESELECT.play();
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        var screenSize = Game.get().getScreenSize();
        var image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        graphics.setColor(new Color(0f, 0f, 0f, 0.8f));
        graphics.fillRect(0, 0, screenSize.width, screenSize.height);
        graphics.setColor(Color.WHITE);
        float textHeight = screenSize.height / 20f;
        var font = Game.get().getFont().deriveFont(textHeight);
        var renders = elements.stream().map(it -> it.render(font, it == elements.get(selected))).toList();
        int renderHeight = renders.stream().mapToInt(BufferedImage::getHeight).sum();
        var ref = new Object() {
            int y = (screenSize.height - renderHeight) / 2;
        };
        renders.forEach(it -> {
            graphics.drawImage(it, (screenSize.width - it.getWidth()) / 2, ref.y, null);
            ref.y += it.getHeight();
        });
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    @Override
    public void refresh() {
    }
}
