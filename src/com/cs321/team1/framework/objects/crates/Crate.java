package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.tiles.Door;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.textures.Textures;

import java.awt.Graphics2D;

public abstract class Crate extends GameObject implements Runnable {
    public Crate(Room room, Location loc) {
        super(room);
        setLocation(loc);
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = collidesWith(UnpassableTile.class) || collidesWith(Door.class) ||
                getCollisions(Crate.class).stream().anyMatch(it -> !canInteractWith(it));
        super.move(-x, -y);
        return !collision;
    }
    
    abstract boolean canInteractWith(Crate other);
    
    abstract String getString();
    
    @Override
    public void run() {
        Crate crate = Game.getPlayer().getGrabbedCrate();
        if (this == crate) setTexture(Textures.CRATE_GRABBED);
        else if (canInteractWith(crate)) setTexture(Textures.CRATE_INTERACTABLE);
        else setTexture(Textures.CRATE);
    }
    
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
