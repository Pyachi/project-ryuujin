package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.menu.elements.MenuButton;

public class OptionsMenu extends LevelMenu {

  @Override
  public void finish() {
  }

  @Override
  public void start() {
    elements.add(new MenuButton("Sound", () -> {
      Sounds.SELECT.play();
      Game.get().pushSegment(new VolumeMenu());
    }));
    elements.add(new MenuButton("Video", () -> {
      Sounds.SELECT.play();
      Game.get().pushSegment(new VideoMenu());
    }));
    elements.add(new MenuButton("Controls", () -> {
      Sounds.SELECT.play();
      Game.get().pushSegment(new ControlsMenu());
    }));
    elements.add(new MenuButton("Back", () -> {
      Sounds.DESELECT.play();
      Game.get().removeSegment(this);
    }));
  }
}
