package com.cs321.team1.objects;

import com.cs321.team1.assets.Texture;
import com.cs321.team1.map.Location;

public class Conveyor extends GameObject {
    private final int x;
    private final int y;
    
    public Conveyor(Location loc, int x, int y) {
        super(1, 1);
        this.x = x;
        this.y = y;
        setLocation(loc);
        setTexture(new Texture("map/floor", 1));
    }
    
    @Tick(priority = 3)
    public void move() {
        getInside(Player.class).forEach(it -> {
            if (it.canMove(x, y)) it.move(x, y);
        });
    }
}
