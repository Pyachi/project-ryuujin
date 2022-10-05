package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.assets.Sounds;

import java.awt.*;

public class LevelMenu extends Menu {
    private final Level level;
    
    public LevelMenu(Level level) {
        this.level = level;
        elements.add(new MenuButton("Resume", () -> {
            Sounds.DESELECT.play();
            Game.get().popSegment();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new OptionsMenu(level));
        }));
        if (Game.get().getSegments().size() > 2) elements.add(new MenuButton("Return to Map", () -> {
            Sounds.DESELECT.play();
            while (Game.get().getSegments().size() > 2) Game.get().popSegment();
        }));
        elements.add(new MenuButton("Quit to Menu", () -> {
            Sounds.DESELECT.play();
            while (Game.get().getSegments().size() > 1) Game.get().popSegment();
        }));
        elements.add(new MenuButton("Quit to Desktop", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
    }
    
    @Override
    public void render(Graphics2D g) {
        if (level != null) level.render(g);
        super.render(g);
    }
    
    @Override
    public void onClose() {
    }
}
