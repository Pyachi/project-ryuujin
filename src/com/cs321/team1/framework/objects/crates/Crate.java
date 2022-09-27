package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.framework.textures.Textures;

import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;

public abstract class Crate extends GameObject implements Runnable {
    private final int value;
    
    public Crate(Level level, Location loc, int value) {
        super(level);
        this.value = value;
        if (loc != null) setLocation(loc);
        setTexture(Textures.CRATE);
    }
    
    public int getValue() {
        return value;
    }
    
    public abstract String getString();
    
    public abstract boolean canInteractWith(Crate crate);
    
    public abstract int getMergedValue(Crate crate);
    
    public boolean canMove(int x, int y) {
        getLocation().move(x, y);
        boolean collision = collidesWith(UnpassableTile.class) ||
                getCollisions(Crate.class).stream().anyMatch(it -> !canInteractWith(it) && !it.canInteractWith(this));
        getLocation().move(-x, -y);
        return !collision;
    }
    
    private void generateNew(Crate crate) {
        if (isDead()) return;
        var location = getLocation().clone();
        if (getRoom().getPlayer().getGrabbedCrate() == this) location = crate.getLocation().clone();
        try {
            crate.getClass()
                    .getDeclaredConstructor(Level.class, Location.class, int.class)
                    .newInstance(getRoom(), location, getMergedValue(crate));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.out.println("This should never happen...");
        }
        Sounds.MERGE.play();
        crate.kill();
        kill();
    }
    
    @Override
    public void run() {
        getCollisions(Crate.class).stream().filter(this::canInteractWith).findAny().ifPresent(this::generateNew);
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.get().getFont().deriveFont(
                (float) 16 * getRoom().getScale() * 0.8F / g.getFontMetrics(Game.get().getFont()).stringWidth(getString())));
        g.drawString(
                getString(),
                getLocation().getX() * getRoom().getScale() - g.getFontMetrics().stringWidth(getString()) / 2 +
                        8 * getRoom().getScale(),
                getLocation().getY() * getRoom().getScale() + g.getFontMetrics().getHeight() / 2 +
                        8 * getRoom().getScale()
        );
    }
}
