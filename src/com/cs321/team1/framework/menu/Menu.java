package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.map.Level;
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
    protected final List<MenuButton> buttons = new ArrayList<>();
    protected final Level level;
    private int selected = 0;
    
    public Menu(Level level) {
        this.level = level;
    }
    
    @Override
    public void update() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) selected++;
        else if (Keyboard.isKeyPressed(KeyEvent.VK_W)) selected--;
        if (selected < 0) selected = buttons.size() - 1;
        else if (selected >= buttons.size()) selected = 0;
        if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) buttons.get(selected).run();
        else if (Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            Game.get().popSegment();
            Sounds.DESELECT.play();
        }
    }
    
    @Override
    public BufferedImage render() {
        Dimension screenSize = Game.get().getScreenSize();
        BufferedImage image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(level.render(), 0, 0, screenSize.width, screenSize.height, null);
        graphics.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        graphics.fillRect(0, 0, screenSize.width, screenSize.height);
        graphics.setColor(Color.WHITE);
        float textHeight = screenSize.height / 20f;
        Font font = Game.get().getFont().deriveFont(textHeight);
        graphics.setFont(font);
        float y = screenSize.height / 2f - (textHeight * (buttons.size()) / 2);
        for (MenuButton button : buttons) {
            int textWidth = graphics.getFontMetrics(font).stringWidth(button.getText());
            if (button == buttons.get(selected)) {
                graphics.setColor(new Color(0.8f, 0.8f, 0.8f, 0.8f));
                graphics.fillRect(
                        (screenSize.width - textWidth) / 2 - 10,
                        (int) (y - textHeight) - 10,
                        textWidth + 20,
                        (int) textHeight + 20
                );
                graphics.setColor(Color.WHITE);
            }
            graphics.drawString(button.getText(), (screenSize.width - textWidth) / 2, (int) y);
            y += textHeight * 2;
        }
        return image;
    }
    
    @Override
    public void refresh() {
    }
}
