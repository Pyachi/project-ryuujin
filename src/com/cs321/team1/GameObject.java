package com.cs321.team1;

import com.cs321.team1.assets.Controls;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.LevelLoader;
import com.cs321.team1.map.Vec2;
import com.cs321.team1.objects.Conveyor;
import com.cs321.team1.objects.PassableTile;
import com.cs321.team1.objects.Player;
import com.cs321.team1.objects.Trigger;
import com.cs321.team1.objects.UnpassableTile;
import com.cs321.team1.objects.crates.DivideCrate;
import com.cs321.team1.objects.crates.IntegerCrate;
import com.cs321.team1.objects.crates.LockedCrate;
import com.cs321.team1.objects.crates.ModuloCrate;
import com.cs321.team1.objects.crates.MultiplyCrate;
import com.cs321.team1.objects.crates.NegateCrate;
import com.cs321.team1.objects.crates.UnpoweredCrate;

import java.awt.Graphics2D;
import java.util.List;

public abstract class GameObject {
    private Level level = null;
    private Vec2 loc = new Vec2(1, 1).toTile();
    private Vec2 size = new Vec2(1, 1).toTile();
    private Texture texture = new Texture("null", -1);
    private int id;
    private boolean dead = false;
    private int tick = 0;
    
    public Level getLevel() {
        return level;
    }
    
    public void setLevel(Level lvl) {
        if (!dead) level = lvl;
    }
    
    public Vec2 getLocation() {
        return loc;
    }
    
    public void setLocation(Vec2 vec) {
        if (!dead) loc = vec;
    }
    
    public Vec2 getSize() {
        return size;
    }
    
    public void setSize(Vec2 vec) {
        if (!dead) size = vec;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public void setTexture(Texture tex) {
        if (!dead) texture = tex;
    }
    
    
    public int getID() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    protected int getTick() {
        return tick;
    }
    
    public void incTick() {
        tick++;
    }
    
    public void move(int x, int y) {
        if (!dead) setLocation(getLocation().add(new Vec2(x, y)));
    }
    
    public void kill() {
        if (level != null) getLevel().removeObject(this);
        texture = null;
        loc = null;
        size = null;
        dead = true;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void paint(Graphics2D g) {
        if (!dead && texture != null) getTexture().paint(this, g, tick);
    }
    
    //******************************************************************************************************************
    //Collision Detection
    
    public boolean collidesWith(Vec2 location) {
        return !isDead() && getLocation().x() < location.x() + 1 &&
                getLocation().x() + getSize().x() > location.x() - 1 && getLocation().y() < location.y() + 1 &&
                getLocation().y() + getSize().y() > location.y() - 1;
    }
    
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        return getLocation().x() < other.getLocation().x() + other.getSize().x() &&
                getLocation().x() + getSize().x() > other.getLocation().x() &&
                getLocation().y() < other.getLocation().y() + other.getSize().y() &&
                getLocation().y() + getSize().y() > other.getLocation().y();
    }
    
    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }
    
    public List<GameObject> getCollisions() {
        return getLevel().getObjects().stream().filter(this::collidesWith).toList();
    }
    
    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
    
    //******************************************************************************************************************
    //Serialization
    
    @Override
    abstract public String toString();
    
    public static GameObject fromString(String obj) {
        try {
            var line = obj.split("\\|");
            var loc = Vec2.fromString(line[1]);
            return switch (line[0]) {
                case "PLR" -> new Player(loc);
                case "INT" -> new IntegerCrate(loc, Integer.parseInt(line[2]));
                case "NEG" -> new NegateCrate(loc);
                case "MOD" -> new ModuloCrate(loc, Integer.parseInt(line[2]));
                case "MUL" -> new MultiplyCrate(loc, Integer.parseInt(line[2]));
                case "DIV" -> new DivideCrate(loc, Integer.parseInt(line[2]));
                case "LCK" -> new LockedCrate(loc, Integer.parseInt(line[2]));
                case "PWR" -> new UnpoweredCrate(loc, Integer.parseInt(line[2]));
                case "CVR" -> switch (line[2]) {
                    default -> Conveyor.UP(loc);
                    case "D" -> Conveyor.DOWN(loc);
                    case "L" -> Conveyor.LEFT(loc);
                    case "R" -> Conveyor.RIGHT(loc);
                };
                case "FLR" -> {
                    if (line[2].contains("/")) yield new PassableTile(loc, Texture.fromString(line[2]));
                    else if (line.length == 4) yield new PassableTile(loc,
                            Vec2.fromString(line[2]),
                            Texture.fromString(line[3]));
                    else yield new PassableTile(loc, Vec2.fromString(line[2]));
                }
                case "WAL" -> {
                    if (line[2].contains("/")) yield new UnpassableTile(loc, Texture.fromString(line[2]));
                    else if (line.length == 4) yield new UnpassableTile(loc,
                            Vec2.fromString(line[2]),
                            Texture.fromString(line[3]));
                    else yield new UnpassableTile(loc, Vec2.fromString(line[2]));
                }
                case "TGR" -> {
                    var cmd = obj.split("->")[1];
                    line = obj.split("->")[0].split("\\|");
                    if (line[2].contains("/")) yield new Trigger(loc, Texture.fromString(line[2]), cmd);
                    else if (line.length == 4) yield new Trigger(loc,
                            Vec2.fromString(line[2]),
                            Texture.fromString(line[3]),
                            cmd);
                    else yield new Trigger(loc, Vec2.fromString(line[2]), cmd);
                }
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
}
