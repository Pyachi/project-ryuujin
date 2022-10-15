package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Music;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelLoader;
import com.cs321.team1.menu.elements.MenuButton;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            LevelLoader.loadLevel("worldOne", true);
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.ERROR.play();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new OptionsMenu(null));
        }));
        elements.add(new MenuButton("Save and Quit", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
        Music.DAY.play();
    }
}
