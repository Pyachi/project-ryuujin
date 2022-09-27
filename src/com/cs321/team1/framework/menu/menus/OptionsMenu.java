package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;

import java.awt.Graphics2D;

public class OptionsMenu extends Menu {
    private final Level level;
    
    public OptionsMenu(Level level) {
        this.level = level;
        elements.add(new MenuButton("Sound Settings", () -> Game.get().pushSegment(new VolumeMenu(level))));
        elements.add(new MenuButton("Video Settings", () -> Game.get().pushSegment(new VideoMenu(level))));
        elements.add(new MenuButton("Controls", () -> Game.get().pushSegment(new ControlsMenu(level))));
        elements.add(new MenuButton("Back", () -> Game.get().popSegment()));
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
