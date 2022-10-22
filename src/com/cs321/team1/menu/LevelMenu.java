package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.assets.audio.filters.Filters;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;

import java.awt.Graphics2D;

public class LevelMenu extends Menu {
    @Override
    public void start() {
        var lvl = Game.getHighestSegmentOfType(Level.class);
        elements.add(new MenuButton("Resume", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new OptionsMenu());
        }));
        if (lvl != null && !lvl.isWorld()) elements.add(new MenuButton("Return to Map", () -> {
            Sounds.DESELECT.play();
            Game.popSegmentsTo(Game.getHighestSegmentOfType(Level.class));
            Game.popSegment();
        }));
        elements.add(new MenuButton("Quit to Menu", () -> {
            Sounds.DESELECT.play();
            Game.saveGame();
            Game.popSegmentsTo(Game.getHighestSegmentOfType(MainMenu.class));
        }));
        elements.add(new MenuButton("Quit to Desktop", () -> {
            Sounds.DESELECT.play();
            Game.saveGame();
            System.exit(0);
        }));
        Music.applyFilter(Filters.MUFFLE);
    }
    
    @Override
    public void finish() {
        Music.applyFilter(null);
    }
    
    @Override
    public void render(Graphics2D g) {
        var lvl = Game.getHighestSegmentOfType(Level.class);
        if (lvl != null) lvl.render(g);
        super.render(g);
    }
}
