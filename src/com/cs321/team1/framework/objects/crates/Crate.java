package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.objects.GameObject;

public abstract class Crate extends GameObject {
    public boolean grabbed = false;

    public Crate(int locX, int locY) {
        super(2);
        setTilePosition(locX, locY);
    }
}
