package com.cs321.team1.menu.elements;

import com.cs321.team1.game.Game;
import com.cs321.team1.util.ResourceUtil;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;

public class MenuText extends MenuElement {

  public MenuText(String text) {
    super(text);
  }

  @Override
  public int getWidth(Font font) {
    return Game.get().renderer.getFontMetrics(font).stringWidth(getText() + "AAAA");
  }

  @Override
  public VolatileImage render(Font font, int timeSelected) {
    FontMetrics fontMetrics = Game.get().renderer.getFontMetrics(font);
    int textHeight = getHeight(font);
    VolatileImage image = ResourceUtil.createImage(Game.get().settings.getScreenSize().x(),
        textHeight);
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
