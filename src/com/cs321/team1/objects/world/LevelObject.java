package com.cs321.team1.objects.world;

import com.cs321.team1.Game;
import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.Vec2;

import java.awt.Graphics2D;

public class LevelObject extends GameObject {
    private final String lvl;
    
    public LevelObject(Vec2 loc, String lvl) {
        this.lvl = lvl;
        setTexture(new Texture("map/level", 2));
        setSize(new Vec2(16, 16));
        setLocation(loc);
    }
    
    @Tick
    public void onTick() {
        if (Game.isLevelCompleted(lvl)) setTexture(new Texture("map/completedLevel",2));
        if (collidesWith(Navigator.class) && Controls.SELECT.isPressed()) Level.load(lvl + "");
    }
    
    @Override
    public String toString() {
        return "LVL|" + getLocation().toString() + "|" + lvl;
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.font()
                .deriveFont((float) 16 * getLevel().getScale() * 0.5F /
                        g.getFontMetrics(Game.font()).stringWidth(lvl + "")));
        g.drawString(lvl + "",
                getLocation().x() * getLevel().getScale() - g.getFontMetrics().stringWidth(lvl + "") / 2 -
                        8 * getLevel().getScale(),
                getLocation().y() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2 -
                        8 * getLevel().getScale());
    }
}
