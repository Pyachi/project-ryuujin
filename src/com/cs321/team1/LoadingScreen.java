package com.cs321.team1;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Music;
import com.cs321.team1.menu.MainMenu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

public class LoadingScreen implements GameSegment {
    private boolean loaded;
    private final Texture tex;
    private int opacity;
    private boolean reverse;
    
    public LoadingScreen() {
        tex = new Texture("splash/splash", 0);
        new Thread(() -> {
            Arrays.stream(Music.values()).forEach(Music::init);
            loaded = true;
        }).start();
    }
    
    @Override
    public void update() {
        if (reverse) {
            opacity-=2;
        } else {
            opacity++;
            opacity = Math.min(opacity, 100);
        }
        if (loaded) reverse = true;
        if (reverse && opacity == 0) {
            Game.popSegment();
            Game.pushSegment(new MainMenu());
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        tex.fillCanvas(g, 0);
        g.setColor(new Color(0F, 0F, 0F, 1F - Math.min(opacity / 100F, 1F)));
        g.fillRect(0, 0, Game.getScreenSize().width, Game.getScreenSize().height);
    }
}
