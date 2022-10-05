package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.map.Worlds;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.assets.Music;
import com.cs321.team1.assets.Sounds;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(Worlds.WORLD_ONE);
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(Worlds.WORLD_ONE);
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
