package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.LevelLoader;
import com.cs321.team1.menu.elements.MenuButton;

import java.io.File;

public class MainMenu extends Menu {
    public MainMenu() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            if (((MenuButton) elements.get(1)).isDisabled()) {
                LevelLoader.loadLevel("worldOne");
                Music.OVERWORLD.play();
            }
            else Game.pushSegment(new NewGameMenu());
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
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
    
    @Override
    public void update() {
        super.update();
        ((MenuButton) elements.get(1)).setDisabled(!new File("ryuujin.sav").exists());
    }
}
