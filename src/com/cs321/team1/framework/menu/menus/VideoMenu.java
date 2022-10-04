package com.cs321.team1.framework.menu.menus;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.menu.Menu;
import com.cs321.team1.framework.menu.elements.MenuButton;
import com.cs321.team1.framework.menu.elements.MenuSlider;
import com.cs321.team1.framework.sounds.Sounds;

import java.awt.*;

public class VideoMenu extends Menu {
    private final Level level;
    
    public VideoMenu(Level level) {
        this.level = level;
        elements.add(new MenuSlider("Resolution:", 0, 10, i -> {
        }));
        elements.add(new MenuButton("Toggle Fullscreen", () -> Game.get().toggleFullscreen()));
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
