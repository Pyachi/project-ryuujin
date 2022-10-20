package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Music;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.map.LevelLoader;
import com.cs321.team1.menu.elements.MenuButton;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            Music.OVERWORLD.play();
            LevelLoader.loadLevel("worldOne");
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
            Music.OVERWORLD.play();
            Game.load();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new OptionsMenu(null));
        }));
        elements.add(new MenuButton("Quit", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
        Music.DAY.play();
    }
}
