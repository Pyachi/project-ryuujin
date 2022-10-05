package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.Menu;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSlider;
import com.cs321.team1.assets.Music;
import com.cs321.team1.assets.Sounds;

import java.awt.*;

public class VolumeMenu extends Menu {
    private final Level level;
    
    public VolumeMenu(Level level) {
        this.level = level;
        elements.add(new MenuSlider("Music Volume:", Music.getVolume() / 10, 10, i -> {
            Music.setVolume(i * 10);
        }));
        elements.add(new MenuSlider("Sound Volume:", Sounds.getVolume() / 10, 10, i -> {
            Sounds.setVolume(i * 10);
            Sounds.SELECT.play();
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
