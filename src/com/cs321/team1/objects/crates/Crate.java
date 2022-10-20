package com.cs321.team1.objects.crates;

import com.cs321.team1.Game;
import com.cs321.team1.assets.Sounds;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.Particle;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Tick;
import com.cs321.team1.objects.UnpassableTile;

import java.awt.Graphics2D;

public abstract class Crate extends GameObject {
    private final int value;

    public Crate(Location loc, int value) {
        this.value = value;
        if (loc != null) setLocation(loc);
        setTexture(new Texture("crates/crate", 1));
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
        move(x, y);
        boolean collision = getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this)
                || collidesWith(UnpassableTile.class)
                || getCollisions(Crate.class).stream()
                .anyMatch(it -> !canInteractWith(it) && !it.canInteractWith(this));
        move(-x, -y);
        return !collision;
    }

    public boolean isGrabbed() {
        return getLevel().getObjects()
                .stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .anyMatch(it -> it.getGrabbedCrate() == this);
    }

    private void generateNew(Crate crate) {
        if (isDead()) return;
        var location = getLocation();
        if (getLevel().getObjects()
                .stream()
                .filter(Player.class::isInstance)
                .anyMatch(it -> ((Player) it).getGrabbedCrate() == this)) location = crate.getLocation();
        Crate newCrate = getMergedCrate(location, crate);
        if (newCrate != null) {
            getLevel().addObject(newCrate);
            getLevel().addObject(new Particle(getLocation(), new Texture("crates/explosion_animated", 4)));
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
                getLocation().x() * getLevel().getScale()
                        - g.getFontMetrics().stringWidth(getString()) / 2
                        - 8 * getLevel().getScale(),
                getLocation().y() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2
                        - 8 * getLevel().getScale());
    }
}
