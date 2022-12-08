package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuText;

public class NewGameConfirmationMenu extends Menu {

  @Override
  public void start() {
    Game game = Game.get();
    elements.add(new MenuText("Create New Game? (This will erase your save file)"));
    elements.add(new MenuButton("Yes, I am ready!", () -> {
      Sounds.SELECT.play();
      game.resetCompletedLevels();
      var lvl = Level.load("world");
      if (lvl != null) {
        game.pushSegments(new LevelTransition(this, lvl), lvl);
      }
      game.removeSegment(this);
    }));
    elements.add(new MenuButton("No wait...", () -> {
      Sounds.DESELECT.play();
      game.removeSegment(this);
    }));
  }
}
