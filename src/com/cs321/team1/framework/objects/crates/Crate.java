package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.objects.GameObject;
import com.cs321.team1.framework.objects.tiles.Door;
import com.cs321.team1.framework.objects.tiles.UnpassableTile;

public abstract class Crate extends GameObject {
    public boolean grabbed = false;
    
    public Crate(Room room, int locX, int locY) {
        super(room);
        getLocation().setTile(locX, locY);
    }
    
    public boolean canMove(int x, int y) {
        super.move(x, y);
        boolean collision = collidesWith(UnpassableTile.class) || collidesWith(Door.class);
        super.move(-x, -y);
        return !collision;
    }
}
