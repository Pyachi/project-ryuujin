package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.sounds.Sounds;

public class LevelMenu extends Menu {
    public LevelMenu(Level level) {
        super(level);
        buttons.add(new MenuButton("Resume", () -> {
            Game.get().popSegment();
            Sounds.DESELECT.play();
        }));
        buttons.add(new MenuButton("Options", () -> {
            Game.get().pushSegment(new OptionsMenu(level));
            Sounds.SELECT.play();
        }));
        buttons.add(new MenuButton("Return to Map", () -> {
            Sounds.ERROR.play();
        }));
        buttons.add(new MenuButton("Quit to Menu", () -> {
            Sounds.ERROR.play();
        }));
        buttons.add(new MenuButton("Quit to Desktop", () -> {
            System.exit(0);
        }));
    }
    
    @Override
    public void onClose() {
    }
}
