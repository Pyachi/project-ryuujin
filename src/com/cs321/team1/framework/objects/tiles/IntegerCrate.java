package com.cs321.team1.framework.objects.tiles;

import com.cs321.team1.framework.Game;
import com.cs321.team1.framework.Textures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class IntegerCrate extends Crate implements Runnable {
    private int value;

    public IntegerCrate(int locX, int locY, int value) {
        super(locX, locY);
        this.value = value;
        texture = getTexture();
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
            it.cleanup();
        });
        texture = getTexture();
    }

    private Textures getTexture() {
        return switch (value) {
            default -> Textures.CRATE_ONE;
            case 2 -> Textures.CRATE_TWO;
            case 3 -> Textures.CRATE_THREE;
        };
    }
}
