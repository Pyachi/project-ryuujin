package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;

import java.awt.*;

public class LevelMenu extends Menu {
    private final Level level;

    public LevelMenu(Level level) {
        this.level = level;
        elements.add(new MenuButton("Resume", () -> Game.get().popSegment()));
        elements.add(new MenuButton("Options", () -> Game.get().pushSegment(new OptionsMenu(level))));
        if (Game.get().getSegments().size() > 2) elements.add(new MenuButton("Return to Map", () -> {
            while (Game.get().getSegments().size() > 2) Game.get().popSegment();
        }));
        elements.add(new MenuButton("Quit to Menu", () -> {
            while (Game.get().getSegments().size() > 1) Game.get().popSegment();
        }));
        elements.add(new MenuButton("Quit to Desktop", () -> System.exit(0)));
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
