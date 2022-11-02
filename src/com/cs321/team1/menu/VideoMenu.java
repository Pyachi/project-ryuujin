package com.cs321.team1.menu;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Resolutions;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSlider;

import java.awt.GraphicsEnvironment;

public class VideoMenu extends LevelMenu {
    
    private MenuSlider fullscreenButton;
    private MenuSlider resolutionButton;
    private MenuSlider monitorButton;
    private MenuSlider fpsButton;
    private MenuButton applyButton;
    
    private int monitor;
    private int prevMonitor;
    
    private Resolutions res;
    private Resolutions prevRes;
    
    private boolean fullscreen;
    private boolean prevFullscreen;
    
    private int fps;
    private int prevFps;
    
    @Override
    public void start() {
        resetSettings();
        fullscreenButton = new MenuSlider("", fullscreen ? 1 : 0, 1, false, i -> {
            fullscreen = i == 1;
            updateButtons();
        });
        monitorButton = new MenuSlider("",
                monitor,
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length - 1,
                false,
                i -> {
                    monitor = i;
                    updateButtons();
                });
        resolutionButton = new MenuSlider("", res.ordinal(), Resolutions.values().length - 1, false, i -> {
            res = Resolutions.values()[i];
            updateButtons();
        });
        fpsButton = new MenuSlider("", fps, 24, false, i -> {
            fps = i;
            updateButtons();
        });
        applyButton = new MenuButton("Apply Settings", () -> {
            if (applySettings()) Sounds.SELECT.play();
            else Sounds.ERROR.play();
        });
        elements.add(fullscreenButton);
        if (fullscreen) elements.add(monitorButton);
        else elements.add(resolutionButton);
        elements.add(fpsButton);
        elements.add(applyButton);
        elements.add(new MenuButton("Back", () -> {
            Sounds.DESELECT.play();
            Game.popSegment();
        }));
        updateButtons();
    }
    
    @Override
    public void finish() { }
    
    private void updateButtons() {
        fullscreenButton.setText(21, "Mode:", (fullscreen ? "Fullscreen" : "Windowed"));
        elements.remove(1);
        if (fullscreen) {
            elements.add(1, monitorButton);
            monitorButton.setText(21, "Monitor:", "" + (monitor + 1));
        } else {
            elements.add(1, resolutionButton);
            resolutionButton.setText(21, "Resolution:", res.name().replaceAll("_", ""));
        }
        fpsButton.setText(21, "FPS: ", fps == 0 ? "VSYNC" : (fps * 10) + "");
        applyButton.setDisabled(
                prevFullscreen == fullscreen && prevMonitor == monitor && prevRes == res && prevFps == fps);
    }
    
    private boolean applySettings() {
        if (prevFullscreen != fullscreen || prevMonitor != monitor || prevRes != res || prevFps != fps) {
            Game.setFullscreen(fullscreen);
            if (fullscreen) Game.setMonitor(monitor);
            else Game.setScreenSize(res.size);
            Game.setFPS(fps * 10);
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
        fps = prevFps = Game.getFPS() / 10;
    }
}
