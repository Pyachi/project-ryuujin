package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.GameObject;

import java.awt.Graphics2D;

public abstract class Crate extends GameObject {
    public Crate(Room room, Location loc) {
        super(room);
        setLocation(loc);
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = shouldCollide();
        super.move(-x, -y);
        return !collision;
    }
    
    abstract boolean shouldCollide();
    
    abstract String getString();
    
    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setFont(Game.font.deriveFont(
                (float) Game.tileSize * Game.scale * 0.8F / g.getFontMetrics(Game.font).stringWidth(getString())));
        g.drawString(
                getString(),
                getLocation().getX() * Game.scale - g.getFontMetrics().stringWidth(getString()) / 2 + 8 * Game.scale,
                getLocation().getY() * Game.scale + g.getFontMetrics().getHeight() / 2 + 8 * Game.scale
        );
    }
}
