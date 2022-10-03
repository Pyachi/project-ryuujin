package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.map.Level;
import com.cs321.team1.framework.map.Location;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.Player;
import com.cs321.team1.framework.objects.intr.Tick;
import com.cs321.team1.framework.objects.intr.Tickable;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;
import com.cs321.team1.framework.sounds.Sounds;
import com.cs321.team1.framework.textures.Textures;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public abstract class Crate extends GameObject implements Tickable {
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
        boolean collision = getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this) || collidesWith(UnpassableTile.class) ||
                getCollisions(Crate.class).stream().anyMatch(it -> !canInteractWith(it) && !it.canInteractWith(this));
        getLocation().move(-x, -y);
        return !collision;
    }

    private void generateNew(Crate crate) {
        if (isDead()) return;
        var location = getLocation().clone();
        if (getRoom().getObjects().stream().filter(Player.class::isInstance).anyMatch(it -> ((Player) it).getGrabbedCrate() == this))
            location = crate.getLocation().clone();
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

    @Tick(priority = 1)
    public void tick() {
        getCollisions(Crate.class).stream().filter(this::canInteractWith).findAny().ifPresent(this::generateNew);
        List<Crate> crates = getRoom().getObjects().stream().filter(Player.class::isInstance).map(Player.class::cast).map(Player::getGrabbedCrate).filter(Objects::nonNull).toList();
        if (crates.stream().anyMatch(this::equals))
            setTexture(Textures.CRATE_GRABBED);
        else if (crates.stream().anyMatch(it -> it.canInteractWith(this) || canInteractWith(it)))
            setTexture(Textures.CRATE_INTERACTABLE);
        else setTexture(Textures.CRATE);
    }

    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.get().getFont().deriveFont(
                (float) 16 * getRoom().getScale() * 0.8F / g.getFontMetrics(Game.get().getFont()).stringWidth(getString())));
        g.drawString(
                getString(),
                getLocation().getX() * getRoom().getScale() - g.getFontMetrics().stringWidth(getString()) / 2 -
                        8 * getRoom().getScale(),
                getLocation().getY() * getRoom().getScale() + g.getFontMetrics().getHeight() / 2 -
                        8 * getRoom().getScale()
        );
    }
}
