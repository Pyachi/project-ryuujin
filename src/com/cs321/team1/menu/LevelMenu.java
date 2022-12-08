package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.assets.audio.filters.Filters;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.menu.elements.MenuButton;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LevelMenu extends Menu {

  @Override
  public void finish() {
    Music.applyFilter(null);
  }

  @Override
  public void start() {
    Game game = Game.get();
    Level lvl = game.getHighestSegmentOfType(Level.class);
    if (lvl == null) {
      return;
    }
    elements.add(new MenuButton("Resume", () -> {
      Sounds.DESELECT.play();
      game.removeSegment(this);
    }));
    elements.add(new MenuButton("Options", () -> {
      Sounds.SELECT.play();
      game.pushSegment(new OptionsMenu());
    }));
    if (!lvl.isWorld) {
      elements.add(new MenuButton("Restart Level", () -> {
        Sounds.SELECT.play();
        game.popSegmentsTo(Level.class);
        Level newLVL = Level.load(lvl.name);
        if (newLVL != null) {
          game.pushSegments(new LevelTransition(lvl, newLVL), newLVL);
          game.removeSegment(lvl);
        }
      }));
      elements.add(new MenuButton("Return to Map", () -> {
        Sounds.DESELECT.play();
        game.popSegmentsTo(Level.class);
        game.pushSegment(new LevelTransition(lvl, game.getSegmentAtIndex(1)));
        game.removeSegment(lvl);
      }));
    }
    elements.add(new MenuButton("Quit to Menu", () -> {
      Sounds.DESELECT.play();
      game.saveGame();
      game.popSegmentsTo(MainMenu.class);
      game.pushSegment(new LevelTransition(lvl, Game.get().getHighestSegment()));
    }));
    elements.add(new MenuButton("Quit to Desktop", () -> {
      Sounds.DESELECT.play();
      game.saveGame();
      System.exit(0);
    }));
    Music.applyFilter(Filters.MUFFLE);
  }

  @Override
  public BufferedImage render() {
    BufferedImage image = Game.get().getRenderingManager().createImage();
    Graphics2D g = image.createGraphics();
    Level lvl = Game.get().getHighestSegmentOfType(Level.class);
    if (lvl == null) {
      return super.render();
    }
    BufferedImage lvlImage = lvl.getLevelImage();
    g.drawImage(lvlImage, (image.getWidth() - lvlImage.getWidth()) / 2,
        (image.getHeight() - lvlImage.getHeight()) / 2, lvlImage.getWidth(), lvlImage.getHeight(),
        null);
    g.drawImage(super.render(), 0, 0, image.getWidth(), image.getHeight(), null);
    return image;
  }
}
