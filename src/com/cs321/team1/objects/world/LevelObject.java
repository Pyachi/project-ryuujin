package com.cs321.team1.objects.world;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelTransition;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.Tick;

import java.awt.Graphics2D;

/**
 * Object used to enter a level from the world map
 */
public class LevelObject extends GameObject {
    private final String lvl;
    
    /**
     * Creates a level object at the given location with the given level
     *
     * @param loc The location of the level object
     * @param lvl The level loaded when interacted with
     */
    public LevelObject(Vec2 loc, String lvl) {
        this.lvl = lvl;
        setTexture(new Texture("map/level", 2));
        setSize(new Vec2(16, 16));
        setLocation(loc);
    }
    
    /**
     * Changes the texture of the level object when level is completed, and handles interaction
     */
    @Tick
    public void onTick() {
        if (Game.get().isLevelCompleted(lvl)) setTexture(new Texture("map/completedLevel", 2));
        if (getCollisions(Navigator.class).stream().anyMatch(it -> it.getLocation().equals(getLocation())) &&
                Controls.SELECT.isPressed()) {
            var level = Level.load(lvl);
            if (level != null) Game.get().pushSegments(new LevelTransition(getLevel(), level), level);
        }
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.get()
                .getRenderingManager()
                .getFont()
                .deriveFont((float) 16 * getLevel().getScale() * 0.5F /
                        g.getFontMetrics(Game.get().getRenderingManager().getFont()).stringWidth(lvl + "")));
        g.drawString(lvl + "",
                getLocation().x() * getLevel().getScale() - g.getFontMetrics().stringWidth(lvl + "") / 2 -
                        8 * getLevel().getScale(),
                getLocation().y() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2 -
                        8 * getLevel().getScale());
    }
    
    @Override
    public String toString() {
        return "LVL|" + getLocation().toString() + "|" + lvl;
    }
}
