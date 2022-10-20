package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;

import java.awt.Graphics2D;

public class OptionsMenu extends Menu {
    private final Level level;

    public OptionsMenu(Level level) {
        this.level = level;
        elements.add(new MenuButton("Sound Settings", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new VolumeMenu(level));
        }));
        elements.add(new MenuButton("Video Settings", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new VideoMenu(level));
        }));
        elements.add(new MenuButton("Controls", () -> {
            Sounds.SELECT.play();
            Game.pushSegment(new ControlsMenu(level));
        }));
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
    }

    @Override
    public void render(Graphics2D g) {
        if (level != null) level.render(g);
        super.render(g);
    }
}
