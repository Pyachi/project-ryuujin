package com.cs321.team1.objects.world;

import com.cs321.team1.GameObject;
import com.cs321.team1.Tick;
import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Vec2;

import java.awt.Graphics2D;

public class NavPath extends GameObject {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    
    public NavPath(Vec2 loc) {
        setSize(new Vec2(16, 16));
        setLocation(loc);
    }
    
    @Tick
    public void checkDirections() {
        move(0,-16);
        up = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
        move(0,32);
        down = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
        move(0,-16);
        move(-16,0);
        left = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
        move(32,0);
        right = collidesWith(LevelObject.class) || collidesWith(NavPath.class);
        move(-16,0);
    }
    
    @Override
    public String toString() {
        return "PTH|" + getLocation().toString();
    }
    
    @Override
    public void paint(Graphics2D g) {
        setTexture(new Texture("map/path",2));
        super.paint(g);
        if (up) {
            setTexture(new Texture("map/path_u",2));
            super.paint(g);
        }
        if (down) {
            setTexture(new Texture("map/path_d",2));
            super.paint(g);
        }
        if (left) {
            setTexture(new Texture("map/path_l",2));
            super.paint(g);
        }
        if (right) {
            setTexture(new Texture("map/path_r",2));
            super.paint(g);
        }
    }
}
