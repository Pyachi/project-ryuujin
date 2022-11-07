package com.cs321.team1.objects.crates;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.game.Game;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.GameObject;
import com.cs321.team1.objects.Particle;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Tick;
import com.cs321.team1.objects.UnpassableTile;

import java.awt.Graphics2D;

/**
 * Abstract class defining basic movable-box behavior and functionality
 */
public abstract class Crate extends GameObject {
    private final int value;
    
    /**
     * Creates a crate at the given location, with the given texture and value
     *
     * @param loc         The location of the crate
     * @param texturePath The texture path of the crate
     * @param value       The internal value of the crate
     */
    public Crate(Vec2 loc, String texturePath, int value) {
        this.value = value;
        setLocation(loc);
        setSize(new Vec2(1, 1).toTile());
        setTexture(new Texture(texturePath, 1));
    }
    
    /**
     * Abstract method checking if crate can interact with other crate
     *
     * @param other The other crate to check interaction with
     * @return True if this crate can interact with the other, false otherwise
     */
    public abstract boolean canBeAppliedTo(Crate other);
    
    /**
     * Checks if crate can be grabbed by the player
     *
     * @return True if crate can be grabbed by the player, false otherwise
     */
    public boolean canGrab() {
        return true;
    }
    
    /**
     * Checks if crate can be moved by (x,y)
     *
     * @param x left-right direction
     * @param y up-down direction
     * @return True if crate can safely move to new position, false if collision detected along the way
     */
    public boolean canMove(int x, int y) {
        move(x, y);
        boolean collision = getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this) ||
                collidesWith(UnpassableTile.class) || getCollisions(Crate.class).stream().anyMatch(it ->
                !canBeAppliedTo(it) && !it.canBeAppliedTo(this));
        move(-x, -y);
        return !collision;
    }
    
    /**
     * Checks if there is a collision with a crate, and merge them if so
     */
    @Tick(priority = 2)
    public void checkMerge() {
        getCollisions(Crate.class).stream().filter(this::canBeAppliedTo).findAny().ifPresent(this::generateNew);
    }
    
    /**
     * Gets the player currently grabbing the crate
     *
     * @return The player grabbing the crate, or null if not grabbed
     */
    public Player getGrabber() {
        return getLevel().getObjects().stream().filter(Player.class::isInstance).map(Player.class::cast).filter(it ->
                it.getGrabbedCrate() == this).findFirst().orElse(null);
    }
    
    /**
     * Abstract method defining new crate when two are merged together
     *
     * @param loc   The location of the new crate
     * @param other The crate being merged with this one
     * @return A new crate with the merged values of the two
     */
    public abstract Crate getMergedCrate(Vec2 loc, Crate other);
    
    /**
     * Gets the display of the crate
     *
     * @return The display of the crate
     */
    public String getString() {
        return Integer.toString(getValue());
    }
    
    /**
     * Gets the internal value of the crate
     *
     * @return The internal value of the crate
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Checks if the crate is grabbed or not
     *
     * @return True if the crate is grabbed, false otherwise
     */
    public boolean isGrabbed() {
        return getGrabber() != null;
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        g.setFont(Game.get()
                .getRenderingManager()
                .getFont()
                .deriveFont((float) 16 * getLevel().getScale() * 0.4F /
                        g.getFontMetrics(Game.get().getRenderingManager().getFont()).stringWidth(getString())));
        g.drawString(getString(),
                getLocation().x() * getLevel().getScale() - g.getFontMetrics().stringWidth(getString()) / 2 -
                        8 * getLevel().getScale(),
                getLocation().y() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2 -
                        7 * getLevel().getScale());
    }
    
    private void generateNew(Crate other) {
        if (isDead()) return;
        var replacedCrate = (!other.canGrab() || canBeAppliedTo(other)) ? other : this;
        var newCrate = getMergedCrate(replacedCrate.getLocation(), other);
        if (newCrate != null) {
            getLevel().addObject(newCrate);
            getLevel().addObject(new Particle((replacedCrate == this ? other : this).getLocation(),
                    new Texture("crates/explosion_animated", 4)));
        }
        Sounds.MERGE.play();
        if (replacedCrate.isGrabbed()) replacedCrate.getGrabber().setGrabbedCrate(newCrate);
        other.kill();
        kill();
    }
}
