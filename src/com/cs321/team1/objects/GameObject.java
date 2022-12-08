package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Level;
import com.cs321.team1.map.Vec2;
import java.awt.Graphics2D;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GameObject {

  private Level level = null;
  private Vec2 loc = new Vec2(1, 1).toTile();
  private Vec2 size = new Vec2(1, 1).toTile();
  private Texture texture = new Texture("null", -1);
  private int id;
  private boolean dead = false;
  private int tick = 0;

  public boolean collidesWith(Vec2 location) {
    return !isDead() && getLocation().x < location.x + 1
        && getLocation().x + getSize().x > location.x - 1 && getLocation().y < location.y + 1
        && getLocation().y + getSize().y > location.y - 1;
  }

  public boolean collidesWith(GameObject other) {
    if (this == other || isDead() || other.isDead() || getLevel() != other.getLevel()) {
      return false;
    }
    return getLocation().x < other.getLocation().x + other.getSize().x
        && getLocation().x + getSize().x > other.getLocation().x
        && getLocation().y < other.getLocation().y + other.getSize().y
        && getLocation().y + getSize().y > other.getLocation().y;
  }

  public boolean collidesWith(Class<? extends GameObject> clazz) {
    return !getCollisions(clazz).isEmpty();
  }

  public List<GameObject> getCollisions() {
    return getLevel().getObjects().stream().filter(this::collidesWith).collect(Collectors.toList());
  }

  public <T extends GameObject> List<T> getCollisions(Class<T> clazz) {
    return getCollisions().stream().filter(clazz::isInstance).map(clazz::cast)
        .collect(Collectors.toList());
  }

  public int getID() {
    return id;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level lvl) {
    if (!dead) {
      level = lvl;
    }
  }

  public Vec2 getLocation() {
    return loc;
  }

  public void setLocation(Vec2 vec) {
    if (!dead) {
      loc = vec;
    }
  }

  public Vec2 getSize() {
    return size;
  }

  public void setSize(Vec2 vec) {
    if (!dead) {
      size = vec;
    }
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture tex) {
    if (!dead) {
      texture = tex;
    }
  }

  public void incTick() {
    tick++;
  }

  public boolean isDead() {
    return dead;
  }

  public void kill() {
    if (level != null) {
      getLevel().removeObject(this);
    }
    texture = null;
    loc = null;
    size = null;
    dead = true;
  }

  public void move(int x, int y) {
    if (!dead) {
      setLocation(getLocation().add(new Vec2(x, y)));
    }
  }

  public void paint(Graphics2D g) {
    if (!dead && texture != null) {
      getTexture().paint(this, g, tick);
    }
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  abstract public String toString();

  protected int getTick() {
    return tick;
  }
}
