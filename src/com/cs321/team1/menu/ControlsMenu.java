package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.audio.Sounds;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ControlsMenu extends LevelMenu {

  private final Map<Controls, MenuButton> buttons = new EnumMap<>(Controls.class);
  private final Map<Controls, Integer> oldKeys = new EnumMap<>(Controls.class);
  private final Map<Controls, Integer> newKeys = new EnumMap<>(Controls.class);
  private MenuButton applyButton;

  @Override
  public void finish() {
  }

  @Override
  public void start() {
    Arrays.stream(Controls.values()).forEach(it -> {
      newKeys.put(it, it.getKey());
      MenuButton button = new MenuButton("", () -> {
        Sounds.SELECT.play();
        Game.get().pushSegment(new Controls.ControlChanger(it, this));
      });
      buttons.put(it, button);
      elements.add(button);
    });
    MenuButton applyButton = new MenuButton("Apply Settings", () -> {
      Sounds.SELECT.play();
      newKeys.forEach(Controls::setKey);
      oldKeys.clear();
      oldKeys.putAll(newKeys);
      restart();
    });
    this.applyButton = applyButton;
    elements.add(applyButton);
    elements.add(new MenuButton("Back", () -> {
      Sounds.DESELECT.play();
      Game.get().removeSegment(this);
    }));
    oldKeys.putAll(newKeys);
    restart();
  }

  public MenuButton getButton(Controls control) {
    return buttons.get(control);
  }

  public int getNewKey(Controls control) {
    return newKeys.get(control);
  }

  @Override
  public void restart() {
    buttons.forEach((control, button) -> button.setText(20, control.name() + ":",
        Controls.keyNameFromInt(newKeys.get(control))));
    applyButton.setDisabled(newKeys.containsValue(-1) || newKeys.equals(oldKeys));
  }

  public void setNewKey(Controls control, int key) {
    newKeys.put(control, key);
  }
}
