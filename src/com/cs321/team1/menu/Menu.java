package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.menu.elements.MenuElement;
import com.cs321.team1.menu.elements.MenuText;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.Vec2;
import com.cs321.team1.util.audio.Sounds;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Menu implements GameSegment {

  protected final List<MenuElement> elements = new ArrayList<>();
  private int selected = 0;
  private int tick = 0;

  @Override
  public void restart() {
    tick = 0;
  }

  @Override
  public void update() {
    tick++;
    if (Controls.DOWN.isPressed()) {
      tick = 0;
      selected++;
    } else if (Controls.UP.isPressed()) {
      tick = 0;
      selected--;
    }
    if (selected < 0) {
      selected = getSelectableElements().size() - 1;
    } else if (selected >= getSelectableElements().size()) {
      selected = 0;
    }
    getSelectableElements().get(selected).update();
    if (!(this instanceof MainMenu) && Controls.BACK.isPressed()) {
      Game.get().removeSegment(this);
      Sounds.DESELECT.play();
    }
  }

  @Override
  public void render(Graphics2D buffer) {
    Vec2 screenSize = Game.get().settings.getScreenSize();
    buffer.setColor(Color.BLACK);
    buffer.fillRect(0, 0, screenSize.x(), screenSize.y());
    buffer.setColor(Color.WHITE);
    Font font = Game.get().renderer.getFont().deriveFont(((float) getTextSize()));
    List<Image> renders = elements.stream()
        .map(it -> it.render(font, it == getSelectableElements().get(selected) ? tick : -1))
        .toList();
    int renderHeight = renders.stream().mapToInt(it -> it.getHeight(null)).sum();
    int y = (screenSize.y() - renderHeight) / 2;
    for (Image render : renders) {
      buffer.drawImage(render, 0, y, null);
      y += render.getHeight(null);
      render.flush();
    }
  }

  private List<MenuElement> getSelectableElements() {
    return elements.stream().filter(it -> !(it instanceof MenuText)).collect(Collectors.toList());
  }

  private int getTextSize() {
    int maxWidth;
    int totalHeight;
    int textSize = Game.get().settings.getScreenSize().y() / 20 + 1;
    do {
      maxWidth = 0;
      totalHeight = 0;
      textSize--;
      for (MenuElement element : elements) {
        Font font = Game.get().renderer.getFont().deriveFont(((float) textSize));
        int width = element.getWidth(font);
        if (maxWidth < width) {
          maxWidth = width;
        }
        totalHeight += element.getHeight(font);
      }
    } while (maxWidth > Game.get().settings.getScreenSize().x()
        || totalHeight > Game.get().settings.getScreenSize().y());
    return textSize;
  }
}
