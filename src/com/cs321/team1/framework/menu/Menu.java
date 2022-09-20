package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.GameComponent;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.util.Keyboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Menu extends GameComponent {
    private final Type type;
    private final List<MenuButton> buttons = new ArrayList<>();
    private int selected;
    
    public Menu(Type type) {
        this.type = type;
        switch (type) {
            case MAIN, WORLD -> {
            }
            case LEVEL -> {
                buttons.add(new MenuButton("Resume", () -> {
                    Game.get().popSegment();
                    Sounds.DROP.play();
                }));
                buttons.add(new MenuButton("Return to Map", () -> System.exit(0)));
                buttons.add(new MenuButton("Options", () -> {
                    Sounds.PICKUP.play();
                    Game.get().pushSegment(new Menu(Type.OPTIONS));
                }));
                buttons.add(new MenuButton("Return to Main Menu", () -> System.exit(0)));
            }
            case OPTIONS -> {
                buttons.add(new MenuButton("Sound Volume", () -> {
                }));
                buttons.add(new MenuButton("Music Volume", () -> {
                }));
                buttons.add(new MenuButton("Toggle Fullscreen", () -> {
                    Game.get().setFullscreen(true);
                }));
                buttons.add(new MenuButton("Return", () -> {
                    Game.get().popSegment();
                    Sounds.DROP.play();
                }));
            }
        }
        selected = 0;
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
            Sounds.DROP.play();
        }
    }
    
    @Override
    public BufferedImage render() {
        int screenWidth = Game.get().getScreenWidth();
        int screenHeight = Game.get().getScreenHeight();
        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        switch (type) {
            case MAIN, WORLD -> {
            }
            case LEVEL -> {
                graphics.drawImage(
                        Game.get().getSegments().get(Game.get().getSegments().indexOf(this) + 1).render(),
                        0,
                        0,
                        Game.get().getScreenWidth(),
                        Game.get().getScreenHeight(),
                        null
                );
            }
            case OPTIONS -> {
                graphics.drawImage(
                        Game.get().getSegments().get(Game.get().getSegments().indexOf(this) + 2).render(),
                        0,
                        0,
                        Game.get().getScreenWidth(),
                        Game.get().getScreenHeight(),
                        null
                );
            }
        }
        graphics.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        graphics.fillRect(0, 0, screenWidth, screenHeight);
        graphics.setColor(Color.WHITE);
        float textHeight = Game.get().getScreenHeight() / 20f;
        Font font = Game.get().getFont().deriveFont(textHeight);
        graphics.setFont(font);
        float y = Game.get().getScreenHeight() / 2f - (textHeight * (buttons.size()) / 2);
        for (MenuButton button : buttons) {
            int textWidth = graphics.getFontMetrics(font).stringWidth(button.getText());
            if (button == buttons.get(selected)) {
                graphics.setColor(new Color(0.8f, 0.8f, 0.8f, 0.8f));
                graphics.fillRect(
                        (Game.get().getScreenWidth() - textWidth) / 2 - 10,
                        (int) (y - textHeight) - 10,
                        textWidth + 20,
                        (int) textHeight + 20
                );
                graphics.setColor(Color.WHITE);
            }
            graphics.drawString(button.getText(), (Game.get().getScreenWidth() - textWidth) / 2, (int) y);
            y += textHeight * 2;
        }
        return image;
    }
    
    @Override
    public void refresh() {
    }
    
    public enum Type {
        MAIN, WORLD, LEVEL, OPTIONS
    }
}
