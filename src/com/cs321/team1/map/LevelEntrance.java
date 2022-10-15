package com.cs321.team1.map;

import com.cs321.team1.Game;
import com.cs321.team1.GameSegment;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelEntrance implements GameSegment {
    public final Level level;
    public double scale = 0.05;
    
    public LevelEntrance(Level level) {
        this.level = level;
    }
    
    @Override
    public void update() {
        scale += 0.05;
        if (scale >= 1) Game.popSegment();
    }
    
    @Override
    public void render(Graphics2D g) {
        BufferedImage levelImage = new BufferedImage(level.getWidth() * 16 * level.getScale(),
                                                     level.getHeight() * 16 * level.getScale(),
                                                     BufferedImage.TYPE_INT_ARGB);
        level.render(levelImage.createGraphics());
        var image = levelImage.getScaledInstance((int) (levelImage.getWidth() * scale),
                                                 (int) (levelImage.getHeight() * scale),
                                                 Image.SCALE_FAST);
        var width = image.getWidth(null);
        var height = image.getHeight(null);
        g.drawImage(image,
                    (levelImage.getWidth() - width) / 2,
                    (levelImage.getHeight() - height) / 2,
                    image.getWidth(null),
                    image.getHeight(null),
                    null);
    }
    
    @Override
    public void onClose() {
    }
}
