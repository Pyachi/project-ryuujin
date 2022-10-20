package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSlider;

import java.awt.Graphics2D;

public class VideoMenu extends Menu {
    private final Level level;

    public VideoMenu(Level level) {
        this.level = level;
        elements.add(new MenuSlider("Resolution:", 0, 10, i -> {
        }));
        elements.add(new MenuButton("Toggle Fullscreen", () -> Game.get().toggleFullscreen()));
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
