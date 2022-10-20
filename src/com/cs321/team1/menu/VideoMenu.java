package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Resolutions;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSlider;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;

public class VideoMenu extends Menu {
    private final Level level;
    private int monitor;
    private int prevMonitor;
    private Resolutions res;
    private Resolutions prevRes;
    private boolean fullscreen;
    private boolean prevFullscreen;
    
    public VideoMenu(Level level) {
        this.level = level;
        resetSettings();
        elements.add(new MenuSlider("", fullscreen ? 1 : 0, 1, false, i -> {
            fullscreen = i == 1;
            updateButtons();
        }));
        if (fullscreen) elements.add(new MenuSlider("",
                monitor,
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length - 1,
                false,
                i -> {
                    monitor = i;
                    updateButtons();
                }));
        else elements.add(new MenuSlider("", res.ordinal(), Resolutions.values().length - 1, false, i -> {
            res = Resolutions.values()[i];
            updateButtons();
        }));
        elements.add(new MenuButton("Apply Settings", () -> {
            if (applySettings()) Sounds.SELECT.play();
            else Sounds.ERROR.play();
        }));
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
        updateButtons();
    }
    
    private void updateButtons() {
        elements.get(0).setText(21, "Mode:", (fullscreen ? "Fullscreen" : "Windowed"));
        elements.remove(1);
        if (fullscreen) {
            elements.add(1,
                    new MenuSlider("",
                            monitor,
                            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length - 1,
                            false,
                            i -> {
                                monitor = i;
                                updateButtons();
                            }));
            elements.get(1).setText(21, "Monitor:", "" + (monitor + 1));
        } else {
            elements.add(1, new MenuSlider("", res.ordinal(), Resolutions.values().length - 1, false, i -> {
                res = Resolutions.values()[i];
                updateButtons();
            }));
            elements.get(1).setText(21, "Resolution:", res.name().replaceAll("_", ""));
        }
    }
    
    private boolean applySettings() {
        if (prevFullscreen != fullscreen || prevMonitor != monitor || prevRes != res) {
            Game.setFullscreen(fullscreen);
            if (fullscreen) Game.setMonitor(monitor);
            else Game.setScreenSize(res.size);
            Game.updateScreen();
            resetSettings();
            updateButtons();
            return true;
        }
        return false;
    }
    
    private void resetSettings() {
        fullscreen = prevFullscreen = Game.isFullscreen();
        monitor = prevMonitor = Game.getMonitor();
        res = prevRes = Resolutions.fromDimension(Game.getScreenSize());
    }
    
    @Override
    public void render(Graphics2D g) {
        if (level != null) level.render(g);
        super.render(g);
    }
}
