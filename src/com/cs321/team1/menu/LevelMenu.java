package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.assets.audio.filters.Filters;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.menu.elements.MenuButton;

import java.awt.image.BufferedImage;

/**
 * Menu seen when a level or world map is paused. Used mainly to access other menus
 */
public class LevelMenu extends Menu {
    @Override
    public void finish() {
        Music.applyFilter(null);
    }
    
    @Override
    public void start() {
        var lvl = Game.get().getHighestSegmentOfType(Level.class);
        elements.add(new MenuButton("Resume", () -> {
            Sounds.DESELECT.play();
            Game.get().popSegment();
        }));
        elements.add(new MenuButton("Options", () -> {
            Sounds.SELECT.play();
            Game.get().pushSegment(new OptionsMenu());
        }));
        if (lvl != null && !lvl.isWorld) {
            elements.add(new MenuButton("Restart Level", () -> {
                Sounds.SELECT.play();
                Game.get().popSegmentsTo(Level.class);
                Game.get().popSegment();
                Level.load(lvl.name);
            }));
            elements.add(new MenuButton("Return to Map", () -> {
                Sounds.DESELECT.play();
                Game.get().popSegmentsTo(Level.class);
                Game.get().popSegment();
            }));
        }
        elements.add(new MenuButton("Quit to Menu", () -> {
            Sounds.DESELECT.play();
            Game.get().saveGame();
            Game.get().popSegmentsTo(MainMenu.class);
        }));
        elements.add(new MenuButton("Quit to Desktop", () -> {
            Sounds.DESELECT.play();
            Game.get().saveGame();
            System.exit(0);
        }));
        Music.applyFilter(Filters.MUFFLE);
    }
    
    @Override
    public BufferedImage render() {
        var image = Game.get().getRenderingManager().createImage();
        var g = image.createGraphics();
        var lvl = Game.get().getHighestSegmentOfType(Level.class);
        if (lvl == null) return super.render();
        var lvlImage = lvl.getLevelImage();
        g.drawImage(lvlImage,
                (image.getWidth() - lvlImage.getWidth()) / 2,
                (image.getHeight() - lvlImage.getHeight()) / 2,
                lvlImage.getWidth(),
                lvlImage.getHeight(),
                null);
        g.drawImage(super.render(), 0, 0, image.getWidth(), image.getHeight(), null);
        return image;
    }
}
