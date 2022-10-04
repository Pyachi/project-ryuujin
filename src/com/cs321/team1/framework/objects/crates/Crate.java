package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Particle;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.Tick;
import com.cs321.team1.framework.objects.tiles.PassableTile;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.framework.textures.Textures;

import java.awt.*;

public abstract class Crate extends GameObject {
    private final int value;
    
    public Crate(Location loc, int value) {
        this.value = value;
        if (loc != null) setLocation(loc);
        setTexture(Textures.CRATE.get());
    }
    
    public int getValue() {
        return value;
    }
    
    public abstract String getString();
    
    public abstract boolean canInteractWith(Crate crate);
    
    public abstract Crate getMergedCrate(Location loc, Crate crate);
    
    public boolean canGrab() {
        return true;
    }
    
    public boolean canMove(int x, int y) {
        getLocation().move(x, y);
        boolean collision = getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this)
                || collidesWith(UnpassableTile.class)
                || getCollisions(Crate.class).stream()
                                             .anyMatch(it -> !canInteractWith(it) && !it.canInteractWith(this));
        getLocation().move(-x, -y);
        return !collision;
    }
    
    private void generateNew(Crate crate) {
        if (isDead()) return;
        var location = getLocation().clone();
        if (getLevel().getObjects()
                      .stream()
                      .filter(Player.class::isInstance)
                      .anyMatch(it -> ((Player) it).getGrabbedCrate() == this)) location = crate.getLocation().clone();
        Crate newCrate = getMergedCrate(location,crate);
        if (newCrate != null) {
            getLevel().addObject(newCrate);
            getLevel().addObject(new Particle(getLocation(), Textures.EXPLOSION.get()));
        }
        Sounds.MERGE.play();
        crate.kill();
        kill();
    }
    
    @Tick(priority = 2)
    public void checkMerge() {
        getCollisions(Crate.class).stream().filter(this::canInteractWith).findAny().ifPresent(this::generateNew);
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.get()
                      .getFont()
                      .deriveFont((float) 16 * getLevel().getScale() * 0.8F / g.getFontMetrics(Game.get().getFont())
                                                                               .stringWidth(getString())));
        g.drawString(getString(),
                     getLocation().getX() * getLevel().getScale()
                             - g.getFontMetrics().stringWidth(getString()) / 2
                             - 8 * getLevel().getScale(),
                     getLocation().getY() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2
                             - 8 * getLevel().getScale());
    }
}
