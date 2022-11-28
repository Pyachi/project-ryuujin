package com.cs321.team1.map;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.game.Game;
import com.cs321.team1.game.GameSegment;

import java.awt.image.BufferedImage;

public class LevelCompletion implements GameSegment {
    private final Level level;
    private final Texture texture = new Texture("map/completion", 10);
    private int tick = 0;
    
    public LevelCompletion(Level level) { this.level = level; }
    
    @Override
    public BufferedImage render() {
        var lvlImage = level.render();
        var completionImage = new BufferedImage((int) (lvlImage.getWidth() * (tick / 50.0)),
                (int) (lvlImage.getHeight() * (tick / 50.0)),
                BufferedImage.TYPE_INT_ARGB);
        texture.fillImage(completionImage, tick);
        lvlImage.getGraphics().drawImage(completionImage,
                (lvlImage.getWidth() - completionImage.getWidth()) / 2,
                (lvlImage.getHeight() - completionImage.getHeight()) / 2,
                completionImage.getWidth(),
                completionImage.getHeight(),
                null);
        return lvlImage;
    }
    
    @Override
    public void update() {
        if (tick < 50) tick++;
        if (Controls.SELECT.isPressed() && tick == 50) {
            Game.get().pushSegment(new LevelTransition(this, Game.get().getSegmentAtIndex(2)));
            Game.get().removeSegment(Game.get().getHighestSegmentOfType(Level.class));
            Game.get().removeSegment(this);
        }
    }
}
