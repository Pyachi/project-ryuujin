package com.cs321.team1.menu.elements;

import com.cs321.team1.game.Game;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MenuText extends MenuElement {

  public MenuText(String text) {
    super(text);
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
    graphics.setFont(font);
    int x = fontMetrics.stringWidth("AA");
    int y = (image.getHeight() - textHeight / 2) / 2 + fontMetrics.getAscent();
    graphics.drawString(getText(), x, y);
    return image;
  }

  @Override
  public void update() {
  }
}
