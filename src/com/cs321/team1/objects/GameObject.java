package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Dimension;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.Location;
import com.cs321.team1.objects.crates.DivideCrate;
import com.cs321.team1.objects.crates.IntegerCrate;
import com.cs321.team1.objects.crates.LockedCrate;
import com.cs321.team1.objects.crates.ModuloCrate;
import com.cs321.team1.objects.crates.MultiplyCrate;
import com.cs321.team1.objects.crates.NegateCrate;
import com.cs321.team1.objects.crates.UnpoweredCrate;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Abstract base for every in-game entity
 */
public abstract class GameObject {
    private Level level = null;
    private Location location = Location.Tile(1, 1);
    private Dimension size = Dimension.Tile(16, 16);
    private Texture texture = new Texture("null", -1);
    private boolean dead = false;
    protected int tick = 0;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level lvl) {
        if (!dead) level = lvl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location loc) {
        if (!dead) location = loc;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension dim) {
        if (!dead) size = dim;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture tex) {
        if (!dead) texture = tex;
    }

    public void move(int x, int y) {
        if (!dead) setLocation(getLocation().add(x, y));
    }

    public void kill() {
        if (level != null) getLevel().removeObject(this);
        texture = null;
        location = null;
        size = null;
        dead = true;
    }

    @Tick
    public void tick() {
        tick++;
    }

    public boolean isDead() {
        return dead;
    }

    public void paint(Graphics2D g) {
        if (!dead && texture != null) getTexture().paint(this, g, tick);
    }

    @Override
    abstract public String toString();

    public static GameObject fromString(String obj) {
        try {
            var line = obj.split("\\|");
            var loc = Location.fromString(line[1]);
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
                    if (line[3].contains("/")) yield new PassableTile(loc, Texture.fromString(line[2]));
                    else if (line.length == 4)
                        yield new PassableTile(loc, Dimension.fromString(line[2]), Texture.fromString(line[3]));
                    else yield new PassableTile(loc, Dimension.fromString(line[2]));
                }
                case "WAL" -> {
                    if (line[3].contains("/")) yield new UnpassableTile(loc, Texture.fromString(line[2]));
                    else if (line.length == 4)
                        yield new UnpassableTile(loc, Dimension.fromString(line[2]), Texture.fromString(line[3]));
                    else yield new UnpassableTile(loc, Dimension.fromString(line[2]));
                }
                default -> null;
//            case "TGR" -> {
//                var command = file.nextLine().split("\\|");
//                Runnable run = () -> {
//                };
//                switch (command[1]) {
//                    case "LVL" -> run = () -> {
//                        if (Controls.SELECT.isPressed()) loadLevel(command[2]);
//                    };
//                }
//                if (line[3].contains("/"))
//                    level.addObject(new Trigger(loc, Texture.fromString(line[3]), run));
//                else {
//                    var size = Dimension.fromString(line[3]);
//                    if (line.length == 5)
//                        level.addObject(new Trigger(loc, size, Texture.fromString(line[4 ]), run));
//                    else level.addObject(new Trigger(loc, size, run));
//                }
//            }
            };
        } catch (Exception e) {
            return null;
        }
    }

    //******************************************************************************************************************
    //Collision handling

    /**
     * Checks if object collides with point location
     *
     * @param location Chosen point location to check collision with
     * @return True if object collides with point, false otherwise
     */
    public boolean collidesWith(Location location) {
        return !isDead()
                && getLocation().x() < location.x() + 1
                && getLocation().x() + getSize().w() > location.x() - 1
                && getLocation().y() < location.y() + 1
                && getLocation().y() + getSize().h() > location.y() - 1;
    }

    /**
     * Checks if object collides with other object
     *
     * @param other Chosen object to check collision with
     * @return True if both objects collide, false otherwise
     */
    public boolean collidesWith(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        return getLocation().x() < other.getLocation().x() + other.getSize().w()
                && getLocation().x() + getSize().w() > other.getLocation().x()
                && getLocation().y() < other.getLocation().y() + other.getSize().h()
                && getLocation().y() + getSize().h() > other.getLocation().y();
    }

    /**
     * Checks if object collides with any object of specified type
     *
     * @param clazz Chosen type of object to check collision with
     * @return True if object collides with specified type, false otherwise
     */
    public boolean collidesWith(Class<? extends GameObject> clazz) {
        return !getCollisions(clazz).isEmpty();
    }

    /**
     * Creates a list of all objects that collide with this object
     *
     * @return List of collisions with this object
     */
    public List<GameObject> getCollisions() {
        return getLevel().getObjects().stream().filter(this::collidesWith).toList();
    }

    /**
     * Creates a list of all objects of specified type that collide with this object
     *
     * @param clazz Chosen type of object to check collision with
     * @param <T>   Generic used to automatically cast to given type
     * @return List of collisions with this object of given type
     */
    public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
        return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    /**
     * Checks if object is physically touching other object
     * Objects that collide are always touching, but not vice-versa
     *
     * @param other Chosen object to check touching with
     * @return True if both objects touch, false otherwise
     */
    public boolean isTouching(GameObject other) {
        if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) return false;
        int locX = getLocation().x() - 1;
        int locY = getLocation().y() - 1;
        int width = getSize().w() + 2;
        int height = getSize().h() + 2;
        return locX < other.getLocation().x() + other.getSize().w()
                && locX + width > other.getLocation().x()
                && locY < other.getLocation().y() + other.getSize().h()
                && locY + height > other.getLocation().y();
    }

    /**
     * Checks if object is physically touching any object of specified type
     *
     * @param clazz Chosen type to check touching with
     * @return True if object touches any object of specified type, false otherwise
     */
    public boolean isTouching(Class<? extends GameObject> clazz) {
        return !getTouching(clazz).isEmpty();
    }

    /**
     * Creates a list of all objects that touch this object
     *
     * @return List of touching objects
     */
    public List<GameObject> getTouching() {
        return getLevel().getObjects().stream().filter(this::isTouching).toList();
    }

    /**
     * Creates a list of all objects touching this object of specified type
     *
     * @param clazz Chosen type to get list of touching
     * @param <T>   Generic used to automatically cast to chosen type
     * @return List of touching objects of specified type
     */
    public <T extends GameObject> List<T> getTouching(Class<T> clazz) {
        return getTouching().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }

    public boolean isInside(GameObject other) {
        return other.collidesWith(getLocation().add(getSize().w() / 2, getSize().h() / 2));
    }

    public boolean isInside(Class<? extends GameObject> clazz) {
        return !getInside(clazz).isEmpty();
    }

    public List<GameObject> getInside() {
        return getLevel().getObjects().stream().filter(it -> it.isInside(this)).toList();
    }

    public <T extends GameObject> List<T> getInside(Class<T> clazz) {
        return getInside().stream().filter(clazz::isInstance).map(clazz::cast).toList();
    }
}
