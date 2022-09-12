package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.objects.GameObject;

public class Crate extends GameObject {
    public boolean grabbed = false;

    public Crate(int locX, int locY) {
        setTilePosition(locX, locY);
        locZ = 2;
    }
}
