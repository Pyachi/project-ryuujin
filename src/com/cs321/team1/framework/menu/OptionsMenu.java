package com.cs321.team1.framework.menu;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Resolutions;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.sounds.Music;
import com.cs321.team1.framework.sounds.Sounds;

import java.util.Arrays;

public class OptionsMenu extends Menu {
    private int chosenResolution;
    
    public OptionsMenu(Level level) {
        super(level);
        buttons.add(new MenuButton("Music Volume: " + Music.volume + "%", () -> {
            Music.volume += 10;
            Music.volume %= 110;
            buttons.get(0).setText("Music Volume: " + Music.volume + "%");
        }));
        buttons.add(new MenuButton("Sound Volume: " + Sounds.volume + "%", () -> {
            Sounds.volume += 10;
            Sounds.volume %= 110;
            Sounds.SELECT.play();
            buttons.get(1).setText("Sound Volume: " + Sounds.volume + "%");
        }));
        buttons.add(new MenuButton("Resolution: ", () -> {
            if (Game.get().isFullscreen()) return;
            chosenResolution++;
            chosenResolution %= Resolutions.values().length;
            buttons.get(2)
                    .setText("Resolution: " + Resolutions.values()[chosenResolution].size.width + "x" +
                            Resolutions.values()[chosenResolution].size.height);
        }));
        buttons.add(new MenuButton("Toggle Fullscreen", () -> {
            Game.get().toggleFullscreen();
            resetChosenResolution();
        }));
        buttons.add(new MenuButton("Back", () -> {
            Game.get().popSegment();
            Sounds.DESELECT.play();
        }));
        resetChosenResolution();
    }
    
    private void resetChosenResolution() {
        chosenResolution = Arrays.stream(Resolutions.values())
                .filter(it -> it.size.equals(Game.get().getScreenSize()))
                .findAny()
                .orElse(Resolutions._640x480).index;
        buttons.get(2)
                .setText("Resolution: " + Resolutions.values()[chosenResolution].size.width + "x" +
                        Resolutions.values()[chosenResolution].size.height);
    }
    
    @Override
    public void onClose() {
        if (!Game.get().isFullscreen()) Game.get().setScreenSize(Resolutions.values()[chosenResolution].size);
    }
}
