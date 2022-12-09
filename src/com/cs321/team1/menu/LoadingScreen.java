package com.cs321.team1.menu;

import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;
import com.cs321.team1.util.ResourceUtil;
import com.cs321.team1.util.Texture;
import com.cs321.team1.util.audio.Music;
import com.cs321.team1.util.audio.Sounds;
import java.awt.Color;
import java.awt.Graphics2D;

public class LoadingScreen implements GameSegment {

  private final Texture tex;
  private boolean loaded;
  private int opacity;
  private boolean reverse;

  public LoadingScreen() {
    tex = new Texture("splash/splash", 0);
    new Thread(() -> {
      Music.init();
      Sounds.init();
      loaded = true;
    }).start();
  }

  @Override
  public void render(Graphics2D buffer) {
    var image = ResourceUtil.createImage();
    tex.fillImage(image, 0);
    buffer.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    buffer.setColor(new Color(0F, 0F, 0F, 1F - Math.min(opacity / 100F, 1F)));
    buffer.fillRect(0, 0, Game.getSettings().getScreenSize().x(),
        Game.getSettings().getScreenSize().y());
    image.flush();
  }

  @Override
  public void update() {
    if (reverse) {
      opacity -= 2;
    } else {
      opacity++;
      opacity = Math.min(opacity, 100);
    }
    if (loaded && opacity == 100) {
      reverse = true;
    }
    if (reverse && opacity == 0) {
      Game.get().pushSegment(new MainMenu());
      Game.get().removeSegment(this);
    }
  }
}
