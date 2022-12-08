package com.cs321.team1.menu.elements;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MenuButton extends MenuElement {

  private final Runnable run;
  private boolean disabled;

  public MenuButton(String text, Runnable run) {
    super(text);
    this.run = run;
  }

  public MenuButton(int size, String left, String right, Runnable run) {
    super(size, left, right);
    this.run = run;
  }

  @Override
  public int getWidth(Font font) {
    return new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font)
        .stringWidth(getText() + "AAAA");
  }

  @Override
  public BufferedImage render(Font font, int timeSelected) {
    FontMetrics fontMetrics = new BufferedImage(1, 1, 1).createGraphics().getFontMetrics(font);
    int textHeight = getHeight(font);
    BufferedImage image = new BufferedImage(Game.get().getRenderingManager().getScreenSize().x,
        textHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = image.createGraphics();
    if (timeSelected != -1) {
      graphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
      graphics.fillRect(0, 0, Math.min((image.getWidth() / 10 * timeSelected), image.getWidth()),
          image.getHeight());
    }
    graphics.setColor(disabled ? Color.DARK_GRAY : Color.WHITE);
    graphics.setFont(font);
    int x = fontMetrics.stringWidth("AA");
    int y = (image.getHeight() - textHeight / 2) / 2 + fontMetrics.getAscent();
    graphics.drawString(getText(), x, y);
    return image;
  }

  @Override
  public void update() {
    if (Controls.SELECT.isPressed()) {
      if (!disabled) {
        run.run();
      } else {
        Sounds.ERROR.play();
      }
    }
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }
}
