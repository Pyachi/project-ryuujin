package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.util.audio.Music;
import com.cs321.team1.util.audio.Sounds;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenu extends Menu {

  @Override
  public void restart() {
    super.restart();
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
      if (((MenuButton) elements.get(1)).isDisabled()) {
        Level lvl = Level.load("world");
        if (lvl != null) Game.get().pushSegments(new LevelTransition(this, lvl), lvl);
      } else Game.get().pushSegment(new NewGameConfirmationMenu());
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
      if (!lvlStrings[0].equals("")) {
        Arrays.stream(lvlStrings[0].split("\n"))
            .forEach(it -> Game.get().completeLevel(it.split("\\|")[1]));
      }
      List<Level> levels = Arrays.stream(Arrays.copyOfRange(lvlStrings, 1, lvlStrings.length))
          .map(it -> Level.fromString("SET" + it)).toList();
      List<GameSegment> segs = levels.stream().map(GameSegment.class::cast)
          .collect(Collectors.toList());
      segs.add(0, new LevelTransition(this, levels.get(0)));
      Game.get().pushSegments(segs.toArray(new GameSegment[]{}));
    } catch (Exception e) {
      Game.getLogger().severe("Could not load save file!");
    }
  }
}
