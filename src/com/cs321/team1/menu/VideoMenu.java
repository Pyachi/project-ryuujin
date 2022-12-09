package com.cs321.team1.menu;

import com.cs321.team1.game.Framerate;
import com.cs321.team1.game.Game;
import com.cs321.team1.game.Resolution;
import com.cs321.team1.menu.elements.MenuButton;
import com.cs321.team1.menu.elements.MenuSelection;
import com.cs321.team1.util.audio.Sounds;

public class VideoMenu extends LevelMenu {

  private MenuSelection fullscreenButton;
  private MenuSelection resolutionButton;
  private MenuSelection framerateButton;
  private MenuButton applicationButton;
  private boolean isFullscreen;
  private boolean wasFullscreen;
  private Resolution newResolution;
  private Resolution oldResolution;
  private Framerate newFramerate;
  private Framerate oldFramerate;

  @Override
  public void start() {
    resetSettings();
    fullscreenButton = new MenuSelection("", isFullscreen ? 1 : 0, 1, false, i -> {
      isFullscreen = i == 1;
      updateButtons();
    });
    resolutionButton = new MenuSelection("", newResolution.ordinal(),
        Resolution.values().length - 1, false, i -> {
      newResolution = Resolution.values()[i];
      updateButtons();
    });
    framerateButton = new MenuSelection("", newFramerate.ordinal(), Framerate.values().length - 1,
        false, i -> {
      newFramerate = Framerate.values()[i];
      updateButtons();
    });
    applicationButton = new MenuButton("Apply Settings", () -> {
      Sounds.SELECT.play();
      applySettings();
    });
    elements.add(fullscreenButton);
    elements.add(resolutionButton);
    elements.add(framerateButton);
    elements.add(applicationButton);
    elements.add(new MenuButton("Back", () -> {
      Sounds.DESELECT.play();
      Game.get().removeSegment(this);
    }));
    updateButtons();
  }

  private void applySettings() {
    Game.get().settings.setFullscreen(isFullscreen);
    Game.get().settings.setResolution(newResolution);
    Game.get().settings.setFramerate(newFramerate);
    Game.get().renderer.updateScreen();
    resetSettings();
    updateButtons();
  }

  private void resetSettings() {
    isFullscreen = wasFullscreen = Game.get().settings.isFullscreen();
    newResolution = oldResolution = Game.get().settings.getResolution();
    newFramerate = oldFramerate = Game.get().settings.getFramerate();
  }

  private void updateButtons() {
    fullscreenButton.setText(21, "Mode:", (isFullscreen ? "Fullscreen" : "Windowed"));
    resolutionButton.setText(21, "Resolution:",
        newResolution.size.x() + "x" + newResolution.size.y());
    framerateButton.setText(21, "FPS: ", newFramerate.getName());
    applicationButton.setDisabled(wasFullscreen == isFullscreen && oldResolution == newResolution
        && oldFramerate == newFramerate);
  }
}
