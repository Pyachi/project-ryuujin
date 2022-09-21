package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;

public class MainMenu extends Menu {
    public MainMenu() {
        super(null);
        buttons.add(new MenuButton("New Game", () -> Game.get().pushSegment(Level.emptyLevel(13,7))));
        buttons.add(new MenuButton("Continue", () -> {}));
        buttons.add(new MenuButton("Options", () -> Game.get().pushSegment(new OptionsMenu(null))));
        buttons.add(new MenuButton("Save and Quit", () -> System.exit(0)));
    }
    
    @Override
    public void onClose() {
    }
}
