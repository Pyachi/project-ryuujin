package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuText;

import java.io.File;

public class MainMenu extends Menu {
    @Override
    public void start() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            if (((MenuButton) elements.get(1)).isDisabled()) Level.load("world");
            else Game.pushSegment(new Menu() {
                {
                    elements.add(new MenuText("Create New Game? (This will erase your save file)"));
                    elements.add(new MenuButton("Yes, I am ready!", () -> {
                        Sounds.SELECT.play();
                        Game.popSegment();
                        Level.load("world");
                    }));
                    elements.add(new MenuButton("No wait...", () -> {
                        Sounds.DESELECT.play();
                        Game.popSegment();
                    }));
                }
            });
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
            Game.loadGame();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new OptionsMenu());
        }));
        elements.add(new MenuButton("Quit", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
        Music.DAY.play();
    }
    
    @Override
    public void refresh() {
        super.refresh();
        Music.DAY.play();
    }
    
    @Override
    public void update() {
        super.update();
        ((MenuButton) elements.get(1)).setDisabled(!new File("ryuujin.sav").exists());
    }
}
