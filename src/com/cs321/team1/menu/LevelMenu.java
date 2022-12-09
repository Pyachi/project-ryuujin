package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.util.audio.Music;
import com.cs321.team1.util.audio.Sounds;
import com.cs321.team1.util.audio.filters.Filters;
import java.awt.Graphics2D;

public class LevelMenu extends Menu {

  @Override
  public void finish() {
    Music.applyFilter(null);
  }

  @Override
  public void start() {
    Game game = Game.get();
    Level lvl = game.getHighestSegmentOfType(Level.class);
    if (lvl == null) return;
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
        game.removeSegment(lvl);
        game.pushSegment(new LevelTransition(lvl, game.getHighestSegment()));
      }));
    }
    elements.add(new MenuButton("Quit to Menu", () -> {
      Sounds.DESELECT.play();
      game.saveGame();
      game.popSegmentsTo(MainMenu.class);
      game.pushSegment(new LevelTransition(lvl, game.getHighestSegment()));
    }));
    elements.add(new MenuButton("Quit to Desktop", () -> {
      Sounds.DESELECT.play();
      game.saveGame();
      System.exit(0);
    }));
    Music.applyFilter(Filters.MUFFLE);
  }

  @Override
  public void render(Graphics2D buffer) {
    var level = Game.get().getHighestSegmentOfType(Level.class);
    if (level == null) super.render(buffer);
    else {
      level.render(buffer);
      super.render(buffer);
    }
  }
}
