package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSelection;
import com.cs321.team1.util.audio.Music;
import com.cs321.team1.util.audio.Sounds;

public class VolumeMenu extends LevelMenu {

  @Override
  public void finish() {
  }

  @Override
  public void start() {
    elements.add(new MenuSelection("Music Volume", Music.getVolume() / 10, 10, true,
        i -> Music.setVolume(i * 10)));
    elements.add(new MenuSelection("Sound Volume", Sounds.getVolume() / 10, 10, true, i -> {
      Sounds.setVolume(i * 10);
      Sounds.SELECT.play();
    }));
    elements.add(new MenuButton("Back", () -> {
      Sounds.DESELECT.play();
      Game.get().removeSegment(this);
    }));
  }
}
