package com.cs321.team1.menu;

import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSlider;

public class VolumeMenu extends LevelMenu {

  @Override
  public void finish() {
  }

  @Override
  public void start() {
    elements.add(new MenuSlider("Music Volume", Music.getVolume() / 10, 10, true, i -> {
      Music.setVolume(i * 10);
    }));
    elements.add(new MenuSlider("Sound Volume", Sounds.getVolume() / 10, 10, true, i -> {
      Sounds.setVolume(i * 10);
      Sounds.SELECT.play();
    }));
    elements.add(new MenuButton("Back", () -> {
      Sounds.DESELECT.play();
      Game.get().removeSegment(this);
    }));
  }
}
