package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.framework.sounds.Sounds;

import java.awt.*;

public class OptionsMenu extends Menu {
    private final Level level;
    
    public OptionsMenu(Level level) {
        this.level = level;
        elements.add(new MenuButton("Sound Settings", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new VolumeMenu(level));
        }));
        elements.add(new MenuButton("Video Settings", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new VideoMenu(level));
        }));
        elements.add(new MenuButton("Controls", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new ControlsMenu(level));
        }));
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.get().popSegment();
        }));
    }
    
    @Override
    public void render(Graphics2D g) {
        if (level != null) level.render(g);
        super.render(g);
    }
    
    @Override
    public void onClose() {
    }
}
