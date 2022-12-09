package com.cs321.team1.menu.elements;

import com.cs321.team1.game.Game;
import com.cs321.team1.util.Controls;
import com.cs321.team1.util.ResourceUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.function.Consumer;

public class MenuSelection extends MenuElement {

  private final int maxOptions;
  private final boolean showSlider;
  private final Consumer<Integer> run;
  private int selected;

  public MenuSelection(String text, int selected, int options, boolean showSlider,
      Consumer<Integer> run) {
    super(text);
    this.selected = selected;
    this.maxOptions = options;
    this.showSlider = showSlider;
    this.run = run;
  }

  public MenuSelection(int size, String left, String right, int selected, int options,
      boolean showSlider, Consumer<Integer> run) {
    super(size, left, right);
    this.selected = selected;
    this.maxOptions = options;
    this.showSlider = showSlider;
    this.run = run;
  }

  @Override
  public int getWidth(Font font) {
    int stringWidth = Game.get().renderer.getFontMetrics(font).stringWidth(getText() + "AAAA");
    return showSlider ? stringWidth * 2 : stringWidth;
  }

  @Override
  public VolatileImage render(Font font, int timeSelected) {
    FontMetrics fontMetrics = Game.get().renderer.getFontMetrics(font);
    int textWidth = fontMetrics.stringWidth(getText());
    int textHeight = fontMetrics.getHeight();
    VolatileImage image = ResourceUtil.createImage(Game.get().settings.getScreenSize().x(),
        textHeight * 2);
    Graphics2D graphics = image.createGraphics();
    if (selected != -1) {
      graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
      graphics.fillRect(0, 0, Math.min((image.getWidth() / 10 * timeSelected), image.getWidth()),
          image.getHeight());
      graphics.setColor(Color.WHITE);
    }
    graphics.setFont(font);
    int x = fontMetrics.stringWidth("AA");
    int y = (image.getHeight() - textHeight) / 2 + fontMetrics.getAscent();
    graphics.drawString(getText(), x, y);
    if (showSlider) {
      int offset = getWidth(font) / 2;
      graphics.fillRect(offset + x, y - textHeight / 2 - textHeight / 10, textWidth,
          textHeight / 5);
      graphics.fillRect(offset + x + (textWidth / maxOptions) * this.selected, y - textHeight,
          textHeight / 5, textHeight);
    }
    return image;
  }

  @Override
  public void update() {
    if (Controls.LEFT.isPressed()) {
      selected = Math.floorMod(selected - 1, maxOptions + 1);
      run.accept(selected);
    } else if (Controls.RIGHT.isPressed() || Controls.SELECT.isPressed()) {
      selected = (selected + 1) % (maxOptions + 1);
      run.accept(selected);
    }
  }
}
