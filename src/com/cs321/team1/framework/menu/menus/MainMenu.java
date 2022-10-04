package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Worlds;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.framework.sounds.Music;
import com.cs321.team1.framework.sounds.Sounds;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(Worlds.worldOne());
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(Worlds.worldOne());
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new OptionsMenu(null));
        }));
        elements.add(new MenuButton("Save and Quit", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
        Music.DAY.play();
    }
    
    @Override
    public void onClose() {
    }
}
