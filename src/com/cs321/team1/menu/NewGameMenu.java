package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.LevelLoader;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuText;

public class NewGameMenu extends Menu {
    public NewGameMenu() {
        elements.add(new MenuText("Create New Game? (This will erase your save file)"));
        elements.add(new MenuButton("Yes, I am ready!",() -> {
            Sounds.SELECT.play();
            Game.popSegment();
            LevelLoader.loadLevel("worldOne");
            Music.OVERWORLD.play();
        }));
        elements.add(new MenuButton("No wait...", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
    }
}
