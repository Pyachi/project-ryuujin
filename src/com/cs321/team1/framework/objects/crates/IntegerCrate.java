package com.cs321.team1.framework.objects.crates;

import com.cs321.team1.framework.map.Room;
import com.cs321.team1.framework.textures.Textures;

import java.util.List;

public class IntegerCrate extends Crate implements Runnable {
    private int value;
    
    public IntegerCrate(Room room, int locX, int locY, int value) {
        super(room, locX, locY);
        this.value = value;
        refreshTexture();
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void run() {
        if (grabbed) return;
        List<IntegerCrate> list = getCollisions(IntegerCrate.class);
        if (list.isEmpty()) return;
        list.forEach(it -> {
            value += it.getValue();
            it.kill();
        });
        refreshTexture();
    }
    
    private void refreshTexture() {
        setTexture(switch (value) {
            default -> Textures.CRATE_ONE;
            case 2 -> Textures.CRATE_TWO;
            case 3 -> Textures.CRATE_THREE;
        });
    }
}
