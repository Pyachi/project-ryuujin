package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuText;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Menu shown on game startup
 */
public class MainMenu extends Menu {
    @Override
    public void refresh() {
        super.refresh();
        Music.DAY.play();
    }
    
    @Override
    public void update() {
        super.update();
        ((MenuButton) elements.get(1)).setDisabled(!new File("ryuujin.sav").exists());
    }
    
    @Override
    public void start() {
        elements.add(new MenuButton("New Game", () -> {
            Sounds.SELECT.play();
            if (((MenuButton) elements.get(1)).isDisabled()) Level.load("world");
            else Game.get().pushSegment(new Menu() {
                {
                    elements.add(new MenuText("Create New Game? (This will erase your save file)"));
                    elements.add(new MenuButton("Yes, I am ready!", () -> {
                        Sounds.SELECT.play();
                        Game.get().popSegment();
                        Level.load("world");
                    }));
                    elements.add(new MenuButton("No wait...", () -> {
                        Sounds.DESELECT.play();
                        Game.get().popSegment();
                    }));
                }
            });
        }));
        elements.add(new MenuButton("Continue", () -> {
            Sounds.SELECT.play();
            loadGame();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new OptionsMenu());
        }));
        elements.add(new MenuButton("Quit", () -> {
            Sounds.DESELECT.play();
            System.exit(0);
        }));
        Music.DAY.play();
    }
    
    private void loadGame() {
        try {
            var lvlStrings = Files.readString(new File("ryuujin.sav").toPath()).split("SET");
            System.out.println(lvlStrings[0]);
            if (!lvlStrings[0].equals("")) Arrays.stream(lvlStrings[0].split("\n")).forEach(it -> Game.get()
                    .completeLevel(it.split("\\|")[1]));
            var levels = Arrays.stream(Arrays.copyOfRange(lvlStrings, 1, lvlStrings.length)).map(it -> Level.fromString(
                    "SET" + it)).toList();
            for (int x = levels.size() - 1; x >= 0; x--)
                Game.get().pushSegment(levels.get(x));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
