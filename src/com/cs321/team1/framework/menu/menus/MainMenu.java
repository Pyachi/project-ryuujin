package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.framework.sounds.Music;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {}));
        elements.add(new MenuButton("Continue", () -> {}));
        elements.add(new MenuButton("Options", () -> Game.get().pushSegment(new OptionsMenu(null))));
        elements.add(new MenuButton("Save and Quit", () -> System.exit(0)));
        Music.DAY.play();
    }
    
    @Override
    public void onClose() {
    }
}
