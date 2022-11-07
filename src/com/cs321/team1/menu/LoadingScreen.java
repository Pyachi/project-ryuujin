package com.cs321.team1.menu;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Screen shown on game startup used to load all assets into memory in the background
 */
public class LoadingScreen implements GameSegment {
    private final Texture tex;
    private boolean loaded;
    private int opacity;
    private boolean reverse;
    
    /**
     * Creates loading screen and starts game initialization
     */
    public LoadingScreen() {
        tex = new Texture("splash/splash", 0);
        new Thread(() -> {
            Music.init();
            Sounds.init();
            loaded = true;
        }).start();
    }
    
    @Override
    public BufferedImage render() {
        var image = Game.get().getRenderingManager().createImage();
        var graphics = image.createGraphics();
        tex.fillCanvas(graphics, 0);
        graphics.setColor(new Color(0F, 0F, 0F, 1F - Math.min(opacity / 100F, 1F)));
        graphics.fillRect(0,
                0,
                Game.get().getRenderingManager().getScreenSize().x(),
                Game.get().getRenderingManager().getScreenSize().y());
        return image;
    }
    
    @Override
    public void update() {
        if (reverse) {
            opacity -= 2;
        } else {
            opacity++;
            opacity = Math.min(opacity, 100);
        }
        if (loaded && opacity == 100) reverse = true;
        if (reverse && opacity == 0) {
            Game.get().popSegment();
            Game.get().pushSegment(new MainMenu());
        }
    }
}
