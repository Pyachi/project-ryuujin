package com.cs321.team1.objects.crates;

import com.cs321.team1.Game;
import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.assets.audio.Sounds;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.Particle;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.UnpassableTile;

import java.awt.Graphics2D;

public abstract class Crate extends GameObject {
    private final int value;
    private final String texturePath;
    
    public Crate(Vec2 loc, String texturePath, int value) {
        this.value = value;
        this.texturePath = texturePath;
        setLocation(loc);
        setSize(new Vec2(1, 1).toTile());
        setTexture(new Texture(texturePath + "_closed", 1));
    }
    
    public int getValue() {
        return value;
    }
    
    public abstract String getString();
    
    public abstract boolean canBeAppliedTo(Crate other);
    
    public abstract Crate getMergedCrate(Vec2 loc, Crate other);
    
    public boolean canGrab() {
        return true;
    }
    
    public boolean canMove(int x, int y) {
        move(x, y);
        boolean collision = getCollisions(Player.class).stream().anyMatch(it -> it.getGrabbedCrate() != this) ||
                collidesWith(UnpassableTile.class) || getCollisions(Crate.class).stream().anyMatch(it ->
                !canBeAppliedTo(it) && !it.canBeAppliedTo(this));
        move(-x, -y);
        return !collision;
    }
    
    public boolean isGrabbed() {
        return getGrabber() != null;
    }
    
    public Player getGrabber() {
        return getLevel().getObjects().stream().filter(Player.class::isInstance).map(Player.class::cast).filter(it ->
                it.getGrabbedCrate() == this).findFirst().orElse(null);
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
    
    @Tick
    public void openTexture() {
        if (getTick() == 60) setTexture(new Texture(texturePath, 1));
    }
    
    @Tick(priority = 2)
    public void checkMerge() {
        getCollisions(Crate.class).stream().filter(this::canBeAppliedTo).findAny().ifPresent(this::generateNew);
    }
    
    @Override
    public void paint(Graphics2D g) {
        if (isDead()) return;
        super.paint(g);
        if (getTick() < 60) return;
        g.setFont(Game.font()
                .deriveFont((float) 16 * getLevel().getScale() * 0.4F /
                        g.getFontMetrics(Game.font()).stringWidth(getString())));
        g.drawString(getString(),
                getLocation().x() * getLevel().getScale() - g.getFontMetrics().stringWidth(getString()) / 2 -
                        8 * getLevel().getScale(),
                getLocation().y() * getLevel().getScale() + g.getFontMetrics().getHeight() / 2 -
                        7 * getLevel().getScale());
    }
}
